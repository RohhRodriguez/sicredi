package com.sicredi.desafio.cadastros.votacao.controller;

import com.sicredi.desafio.cadastros.assembleia.model.dto.FiltroAssembleiaDTO;
import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import com.sicredi.desafio.cadastros.assembleia.service.AssembleiaService;
import com.sicredi.desafio.cadastros.associado.model.entity.Associado;
import com.sicredi.desafio.cadastros.pauta.model.dto.FiltroPautaDTO;
import com.sicredi.desafio.cadastros.pauta.model.dto.PautaComVotacoesDTO;
import com.sicredi.desafio.cadastros.pauta.model.entity.Pauta;
import com.sicredi.desafio.cadastros.pauta.service.PautaService;
import com.sicredi.desafio.cadastros.votacao.model.dto.FiltroVotacaoDTO;
import com.sicredi.desafio.cadastros.votacao.model.dto.VotacaoResumoDTO;
import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import com.sicredi.desafio.cadastros.votacao.service.VotacaoService;
import com.sicredi.desafio.integracoes.service.IntegracaoService;
import com.sicredi.desafio.utils.enums.PautaStatus;
import com.sicredi.desafio.utils.enums.RegistrationStatus;
import com.sicredi.desafio.utils.enums.VotacaoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FormularioController {
    @Autowired VotacaoService votacaoService;
    @Autowired PautaService pautaService;
    @Autowired AssembleiaService assembleiaService;

    @GetMapping("/form-votacao")
    public RedirectView mostrarFormularioVotacao() {
        return new RedirectView("/formulario.html");
    }

    @GetMapping("/dados-formulario")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> carregarDadosFormulario() {
        Map<String, Object> dadosFormulario = new HashMap<>();

        FiltroAssembleiaDTO assembleiaFiltro = new FiltroAssembleiaDTO();
        assembleiaFiltro.setStatus(RegistrationStatus.ATIVO.name());

        List<Assembleia> assembleiasAtivas = assembleiaService.findAllAssembleiasWithFilter(assembleiaFiltro, Pageable.unpaged()).getContent();
        dadosFormulario.put("assembleias", assembleiasAtivas);

        return ResponseEntity.ok(dadosFormulario);
    }

    @GetMapping("/pautas-abertas")
    @ResponseBody
    public List<PautaComVotacoesDTO> listarPautasComVotacoesAbertas(@RequestParam String codigoAssembleia) {
        // pautas
        FiltroPautaDTO filtroPautaDTO = new FiltroPautaDTO();
        filtroPautaDTO.setStatus(PautaStatus.ATIVA.name());
        filtroPautaDTO.setCodigoAssembleia(codigoAssembleia);
        Pageable pageable = Pageable.unpaged();
        List<Pauta> pautas = pautaService.findAllPautasWithFilter(filtroPautaDTO, pageable).getContent();

        // votações
        FiltroVotacaoDTO filtroVotacaoDTO = new FiltroVotacaoDTO();
        filtroVotacaoDTO.setStatusList(List.of(VotacaoStatus.ABERTA.name()));
        List<Votacao> votacoes = votacaoService.findAllVotacoesWithFilter(filtroVotacaoDTO, pageable).getContent();

        Map<String, List<Votacao>> votacoesPorPauta = votacoes.stream()
                .collect(Collectors.groupingBy(Votacao::getCodigoPauta));

        //so pego as pautas c/ votações abertas.
        List<PautaComVotacoesDTO> resultado = new ArrayList<>();
        for (Pauta pauta : pautas) {
            if (votacoesPorPauta.containsKey(pauta.getCodigo())) {
                List<Votacao> votacoesDaPauta = votacoesPorPauta.get(pauta.getCodigo());
                List<VotacaoResumoDTO> votacaoResumoDTOs = new ArrayList<>();
                for (Votacao vot : votacoesDaPauta) {
                    votacaoResumoDTOs.add(new VotacaoResumoDTO(vot.getCodigo(), vot.getStatus(), vot.getDataHoraInicio()));
                }
                resultado.add(new PautaComVotacoesDTO(pauta.getCodigo(), pauta.getNome(), votacaoResumoDTOs));
            }
        }
        return resultado;
    }

    @GetMapping("/associados-habilitados")
    @ResponseBody
    public ResponseEntity<List<Associado>> listarAssociadosHabilitados(@RequestParam String codigoPauta) {
        List<Associado> associados = votacaoService.buscarAssociadosHabilitadosParaFormulario(codigoPauta);
        return ResponseEntity.ok(associados);
    }

    @GetMapping("/resultado")
    public String exibirPaginaResultado() {
        return "resultado.html";
    }

}
