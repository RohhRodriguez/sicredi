package com.sicredi.desafio.cadastros.votacao.service;

import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import com.sicredi.desafio.cadastros.assembleia.repository.AssembleiaRepository;
import com.sicredi.desafio.cadastros.associado.model.entity.Associado;
import com.sicredi.desafio.cadastros.associado.repository.AssociadoRepository;
import com.sicredi.desafio.cadastros.pauta.model.entity.Pauta;
import com.sicredi.desafio.cadastros.pauta.repository.PautaRepository;
import com.sicredi.desafio.cadastros.votacao.model.dto.*;
import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import com.sicredi.desafio.cadastros.votacao.model.entity.Voto;
import com.sicredi.desafio.cadastros.votacao.repository.VotacaoRepository;
import com.sicredi.desafio.cadastros.votacao.repository.VotoRepository;
import com.sicredi.desafio.cadastros.votacao.scheduler.SessaoVotacaoFila;
import com.sicredi.desafio.exceptions.VotacaoExceptions;
import com.sicredi.desafio.exceptions.VotacaoMsgExceptions;
import com.sicredi.desafio.integracoes.service.IntegracaoService;
import com.sicredi.desafio.utils.*;
import com.sicredi.desafio.utils.enums.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VotacaoService {
    @Autowired
    VotacaoRepository votacaoRepository;
    @Autowired EntityManager entityManager;
    @Autowired SessaoVotacaoFila filaVotacaoAbertaMockada;
    @Autowired AssociadoRepository associadoRepository;
    @Autowired PautaRepository pautaRepository;
    @Autowired
    AssembleiaRepository assembleiaRepository;
    @Autowired VotoRepository votoRepository;
    @Autowired IntegracaoService integracaoService;

    // Retorna uma lista filtrada ou não de votos (dá para filtrar por pauta ou votação tbm
    public PageResponse<Voto> findAllVotosWithFilter(FiltroVotoDTO filtroDTO, Pageable pageable) {
        Page<Voto> page = DynamicFilterUtil.filter(Voto.class, filtroDTO, entityManager, pageable);
        return PageResponseUtil.fromPage(page);
    }

    // Retorna uma lista filtrada ou não de votações..
    public PageResponse<Votacao> findAllVotacoesWithFilter(FiltroVotacaoDTO filtroDTO, Pageable pageable) {
        if (filtroDTO.getStatusList() != null && !filtroDTO.getStatusList().isEmpty()) {
            for (String status : filtroDTO.getStatusList()) {
                GenericMethods.validateEnumIfPresent(status, VotacaoStatus.class,
                        new VotacaoExceptions.TEStatusInvalidoException(VotacaoMsgExceptions.RANGE_STATUS_NAO_ENCONTRADO, status));
            }
        }
        Page<Votacao> page = DynamicFilterUtil.filter(Votacao.class, filtroDTO, entityManager, pageable);
        return PageResponseUtil.fromPage(page);
    }

    // Criar uma sessao de votacao passando o codigo da pauta selecionada e a duração em minutos (se null = 1 min)
    public Votacao createVotatingSession(SessaoVotacaoDTO dto) {
        assert dto != null;
        // TODO: estou supondo que exista uma regra de negócio onde é possível abrir apenas uma votação por vez por pauta...
        //  Caso seja possível mais de uma votação por vez em cada pauta, comentar essa validação
        List<Votacao> votacoesAbertas = votacaoRepository.findVotacoesAbertasByCodigoPauta(dto.getCodigoPauta());
        if (!votacoesAbertas.isEmpty()) {
            throw new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.JA_EXISTE_VOTACAO_ABERTA_PARA_PAUTA, dto.getCodigoPauta(), votacoesAbertas.getFirst().getDataHoraEncerramento());
        }
        // caso não venha preenchido, será considerado o padrão de 1 min
        DuracaoDTO duracaoDTO = dto.getDuracao() != null ? dto.getDuracao() : new DuracaoDTO(0, 1, 0);

        Pauta pautaExistente = pautaRepository.findByCodigo(dto.getCodigoPauta()).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "Código da pauta", dto.getCodigoPauta()));

        Votacao votacao = new Votacao();
        votacao.setCodigoAssembleia(pautaExistente.getCodigoAssembleia());
        votacao.setCodigoPauta(pautaExistente.getCodigo());
        votacao.setCodigo(UtilMethods.generateNextCode(votacaoRepository, Votacao::getCodigo));

        // TODO: aqui estou supondo que exista uma regra de negócio para permitir a abertura programada de uma sessão de votação...
        //  nesse caso o usuario poderia colocar uma data futura e agendar a abertura... Se nada for passado, considera data e hora atual
        LocalDateTime inicio = dto.getDataHoraInicio() != null ? dto.getDataHoraInicio() : LocalDateTime.now();
        votacao.setDataHoraInicio(inicio);

        votacao.setDataHoraEncerramento(inicio
                .plusHours(duracaoDTO.getHoras())
                .plusMinutes(duracaoDTO.getMinutos())
                .plusSeconds(duracaoDTO.getSegundos())
        );
        votacao.setStatus(VotacaoStatus.ABERTA.name()); // seta como aberta para receber votos
        Votacao salvo = votacaoRepository.save(votacao);

        pautaExistente.setStatus(PautaStatus.ATIVA.name()); // ativa a pauta para receber votos
        pautaRepository.save(pautaExistente);

        // TODO: Ajustar depois para usar fila do rabbitmq, azure ou outras (por enquanto ficando mockada para finalizar outras partes do desafio)
        filaVotacaoAbertaMockada.adicionarSessao(salvo); // add em uma fila para recebimento de votos

        return salvo;
    }


    public Voto saveVoteInSession(VotacaoDTO dto) {
        assert dto != null;
        VotacaoInfoCompletaDTO votacaoInfoCompletaDTO = validaVotacao(dto);

        Voto voto = new Voto();
        voto.setVoto(dto.getVoto()); // já validei se esta dentro do range de enum
        voto.setCodigoVotacao(votacaoInfoCompletaDTO.votacao().getCodigo());
        voto.setCodigoAssociado(votacaoInfoCompletaDTO.associado().getCodigo());
        voto.setCodigo(UtilMethods.generateNextCode(votoRepository, Voto::getCodigo));

        Voto saved = votoRepository.save(voto);

        Votacao votacaoAtualizada = atualizaResumoPauta(votacaoInfoCompletaDTO.votacao(), false);

        return saved;
    }

    private VotacaoInfoCompletaDTO validaVotacao(VotacaoDTO votacaoDTO) {
        try {
            VotoOption.valueOf(votacaoDTO.getVoto());
        } catch (IllegalArgumentException ex) {
            throw new VotacaoExceptions.TEVotacaoInvalidoException(VotacaoMsgExceptions.RANGE_STATUS_NAO_ENCONTRADO, votacaoDTO.getVoto());
        }
        // TODO: Estou pedindo o codigo ao inves do id pq o codigo é unico tbm na tabela, então funciona da mesma forma e fica mais elegante na minha opinião
        Associado associadoExistente = associadoRepository.findByCodigo(votacaoDTO.getCodigoAssociado()).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "Código do associado", votacaoDTO.getCodigoAssociado()));
        // Antes de verificar se o cpf é valido, estou verificando se ele está inativo...Um duplo check
        if (associadoExistente.getStatus().equalsIgnoreCase(RegistrationStatus.INATIVO.name())) {
            throw new VotacaoExceptions.TEVotacaoInvalidoException(VotacaoMsgExceptions.VOTO_ASSOCIADO_INATIVO, associadoExistente.getCodigo(), associadoExistente.getStatus());
        }
        Pauta pautaExistente = pautaRepository.findByCodigo(votacaoDTO.getCodigoPauta()).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "Código da pauta", votacaoDTO.getCodigoPauta()));

        Assembleia assembleiaExistente = assembleiaRepository.findByCodigo(pautaExistente.getCodigoAssembleia()).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "Código da assembléia", pautaExistente.getCodigoAssembleia()));

        // TODO: Verificar se na regra de negócio será permitida reabertura da votaçãi para a mesma pauta no futuro... Se sim, seria importante validar os status
        Votacao votacaoExistente = votacaoRepository.findVotacoesAbertasByCodigoPauta(pautaExistente.getCodigo())
                .stream()
                .filter(votacao -> votacao.getStatus().equalsIgnoreCase(VotacaoStatus.ABERTA.name())) // só deixa votar se a votação estiver aberta
                .findFirst()
                .orElseThrow(() -> new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.VOTACAO_NAO_ABERTA_PARA_PAUTA_INFORMADA, pautaExistente.getCodigo()));

        // Verifico aqui se já existe algum voto para essa votacao realizado pelo associado... Se sim, não permito votar
        Optional<Voto> votoAssociado = votoRepository.findByCodigoVotacaoAndCodigoAssociado(votacaoExistente.getCodigo(), associadoExistente.getCodigo());
        if (votoAssociado.isPresent()) {
            associadoExistente.setStatus(AssociadoStatus.ATIVO.name());
            throw new VotacaoExceptions.TEVotacaoInvalidoException(VotacaoMsgExceptions.VOTO_ASSOCIADO_DUPLICADO, associadoExistente.getCodigo(), votoAssociado.get().getVoto());
        }
        // TODO: Aqui vou usar o método do service de integração para verificar se é ou não habilitado para votação e atualizar o status dele para habilitado
        //  Caso o cpf seja valido, mesmo que dê um erro na hora de atualizar o status, estou deixando registrar o voto... caso não seja permitido pela regra de negócio,
        //  aqui teria que ser ajustado para barrar a continuidade do registro caso o status não seja atualizado...
        Associado associadoAtualizado = associadoExistente;
        try {
            associadoAtualizado = verificaAtualizaStatusAssociado(associadoExistente, votacaoExistente);
        } catch (DataAccessException e) {
            throw new VotacaoExceptions.TEVotacaoInvalidoException("Falha ao atualizar status do associado", associadoExistente.getCodigo(), associadoExistente.getCpf());
        }
        if (!pautaExistente.getStatus().equalsIgnoreCase(PautaStatus.ATIVA.name())) {
            throw new VotacaoExceptions.TEVotacaoInvalidoException(VotacaoMsgExceptions.VOTO_PAUTA_NAO_ATIVA, pautaExistente.getCodigo(), pautaExistente.getStatus());
        }

        return new VotacaoInfoCompletaDTO(associadoAtualizado, assembleiaExistente, pautaExistente, votacaoExistente);
    }

    // encerra a votação e retorna um resumo do resultado atualizado
    @Transactional
    public Votacao encerraVotacao(UUID id) {
        Votacao votacao = votacaoRepository.findById(id).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "ID da votação", id));

        votacao.setStatus(VotacaoStatus.ENCERRADA.name());

        return atualizaResumoPauta(votacao, true);
    }

    // atualiza os totais da votacao e retorna um resumo atualizado
    private Votacao atualizaResumoPauta(Votacao votacao, boolean votacaoEncerrada) {
        List<Voto> votos = votoRepository.findAllByCodigoVotacao(votacao.getCodigo());
        long votosSim = votos.stream().filter(v -> "SIM".equalsIgnoreCase(v.getVoto())).count();
        long votosNao = votos.stream().filter(v -> "NAO".equalsIgnoreCase(v.getVoto())).count();
        votacao.setQuantidadeVotos(votos.size());

        if (votacaoEncerrada) {
            String resultado = "NDA";
            // TODO: Verificar regra de negócio para quando uma pauta fechar sem votos... Aqui estou considerando que os campos ficarão nulos,
            //  caso seja obrigatorio ao menos um voto para fechar, teria que ajustar a regra de negocio para o encerramento se estender (o que ainda não tenho a info)
            if (votos.isEmpty()) {
                votacao.setResultado(resultado);
                votacao.setPercentual(0.0);
            } else {
                resultado = votosSim == votosNao ? "EMP" : (votosSim > votosNao ? VotoOption.SIM.name() : VotoOption.NAO.name());
                long maiorVoto = Math.max(votosSim, votosNao);
                double percentual = (maiorVoto * 100.0) / votos.size();

                votacao.setResultado(resultado);
                votacao.setPercentual(percentual);
            }
            Optional<Pauta> pauta = pautaRepository.findByCodigo(votacao.getCodigoPauta());
            if (pauta.isPresent()) {
                //TODO: verificar se o status da pauta tbm vai ser mudado ou se ela vai ficar disponível para n votacoes... estou supondo q ao encerrar a votação,
                // nao faça mais sentido mantê-la aberta
                pauta.get().setStatus(PautaStatus.CONCLUIDA.name());
                pautaRepository.save(pauta.get());
            }
        }
        return votacaoRepository.save(votacao);
    }

    // busca por id uma Votacao
    public Votacao findVotacaoById(UUID id) {
        return votacaoRepository.findById(id).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "ID da Votação", id));
    }

    private Associado verificaAtualizaStatusAssociado(Associado associado, Votacao votacao) {
        // TODO: Aqui vou usar o método do service de integração para verificar se é ou não habilitado para votação
        boolean cpfValido = integracaoService.validaCPF(associado.getCpf());

        if (!cpfValido || associado.getStatus().equalsIgnoreCase(AssociadoStatus.INATIVO.name()) || associado.getStatus().equalsIgnoreCase(AssociadoStatus.PENDENTE.name())) {
            associado.setStatus(AssociadoStatus.PENDENTE.name()); // seto o status para pendente e a partir de agora ele não poderá mais votar até regularizar o cadasrtro
            throw new VotacaoExceptions.TEVotacaoInvalidoException(VotacaoMsgExceptions.CPF_ASSOCIADO_NAO_HABILITADO, associado.getCodigo(), associado.getCpf());
        } else {
            if (votacao.getStatus().equalsIgnoreCase(VotacaoStatus.ABERTA.name()) && associado.getStatus().equalsIgnoreCase(AssociadoStatus.ATIVO.name())) {
                associado.setStatus(AssociadoStatus.HABILITADO.name()); // seto o status para habilitado somente se a votacao estiver aberta
            } else {
                associado.setStatus(AssociadoStatus.ATIVO.name()); // seto o status padrão caso a votacao estiver fechada já
            }
        }
        return associadoRepository.save(associado);
    }

}
