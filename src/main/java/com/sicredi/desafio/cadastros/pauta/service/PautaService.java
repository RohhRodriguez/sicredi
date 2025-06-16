package com.sicredi.desafio.cadastros.pauta.service;

import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import com.sicredi.desafio.cadastros.assembleia.repository.AssembleiaRepository;
import com.sicredi.desafio.cadastros.pauta.model.dto.FiltroPautaDTO;
import com.sicredi.desafio.cadastros.pauta.model.dto.PautaDTO;
import com.sicredi.desafio.cadastros.pauta.model.entity.Pauta;
import com.sicredi.desafio.cadastros.pauta.repository.PautaRepository;
import com.sicredi.desafio.exceptions.VotacaoExceptions;
import com.sicredi.desafio.exceptions.VotacaoMsgExceptions;
import com.sicredi.desafio.utils.*;
import com.sicredi.desafio.utils.enums.VotacaoStatus;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PautaService {
    @Autowired PautaRepository pautaRepository;
    @Autowired AssembleiaRepository assembleiaRepository;
    @Autowired EntityManager entityManager;

    // Retorna uma lista filtrada ou não de pautas
    public PageResponse<Pauta> findAllPautasWithFilter(FiltroPautaDTO filtroDTO, Pageable pageable) {
        Pageable pageableNew = GenericMethods.validaPageable(pageable);
        if (filtroDTO.getStatusList() != null && !filtroDTO.getStatusList().isEmpty()) {
            for (String status : filtroDTO.getStatusList()) {
                GenericMethods.validateEnumIfPresent(status, VotacaoStatus.class,
                        new VotacaoExceptions.TEStatusInvalidoException(VotacaoMsgExceptions.RANGE_STATUS_NAO_ENCONTRADO, status));
            }
        }
        Page<Pauta> page = DynamicFilterUtil.filter(Pauta.class, filtroDTO, entityManager, pageableNew);
        return PageResponseUtil.fromPage(page);
    }

    // Salva uma pauta
    public Pauta savePauta(PautaDTO dto) {
        Pauta pauta = UtilMethods.mapDtoToEntity(dto, Pauta.class);
        pauta.setCodigo(UtilMethods.generateNextCode(pautaRepository, Pauta::getCodigo));

        Assembleia assembleia = assembleiaRepository.findByCodigo(dto.getCodigoAssembleia()).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "Código da Assembléia: ", dto.getCodigoAssembleia()));
        pauta.setStatus(VotacaoStatus.ATIVA.name()); //todo: alterei para ativa logo no cadastro (alterei para todos os campos serem obrigatorios)

        return pautaRepository.save(pauta);
    }

    // busca por id uma Pauta
    public Pauta findPautaById(UUID id) {
        return pautaRepository.findById(id).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "ID da Pauta: ", id));
    }


}
    /*
    Planejamento das tarefas:
    1. Pastas: assembleia e dentro dela, separar duas etapas: 'pauta' e 'votacao' e criar para cada uma delas as subpastas
    com os controllers, repository, model, etc.. OK

    ● Cadastrar uma nova pauta  OK
    - Cadastrar algum assunto para disponibilizar para que o usuario possa votar na sessao (status pendente inicial até receber algum voto)
    - Metodos: POST: cadastrar a pauta pendente  OK/ GET: Listar as pautas filtradas  OK
    - criar um dto: codigo, nome, descricao, datInicio, dataFim (todos obrigatórios?) para receber os dados OK
    - criar uma migration para criar a tabela de pauta (nome: 'assembleia_pauta') OK

    ● Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default)
    - Criar uma sessao de votacao passando o codigo da pauta selecionada e a duração em minutos (se null = 1 min)
    - Metodos: POST: cadastrar uma sessao nova para receber os votos.. (trocar status para aberta e deixar até acabar o tempo definido ou defautl)
    - preciso colocar tratamentos para as exceções q possam acontecer...
    - criar uma migration para criar a tabela de votacao (nome: 'assembleia_votacao' FK codigoAssembleia)

    ● Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta)
    - tenho que criar um enum para travar as opcoes permitidas e block votacao duplicada do associado
    - criar uma entidade Associado com id, codigo, nome, email (todos not null, id pk)
    - criar uma migration para criar a tabela de associados (nome: 'associados')
    - metodos: POST: cadastrar votos (campos: AssociadoDTO, codigoPauta, voto) - travar votos diferentes de sim e nao conforme o enum
    - validar antes se os dados do associado batem com o que existe na base - lançar exceção se não

    ● Contabilizar os votos e dar o resultado da votação na pauta
    - tenho que criar um metodo get para trazer o resultado da votação...
    - capturar usando o endpoint de votos filtrando por pauta e depois contabilizar os sim e nao e retornar em um dto personalizado
    - campos do dto: dados da pauta (PautaDTO), resultado (qtde de votos, resultado obtido, percentual em relação ao total)
    - posso deixar esses resultados numa tabela separada da pauta..
    */