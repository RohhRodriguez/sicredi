package com.sicredi.desafio.cadastros.votacao.controller;

import com.sicredi.desafio.cadastros.pauta.service.PautaService;
import com.sicredi.desafio.cadastros.votacao.model.dto.*;
import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import com.sicredi.desafio.cadastros.votacao.service.VotacaoService;
import com.sicredi.desafio.exceptions.VotacaoExceptions;
import com.sicredi.desafio.utils.enums.VotacaoStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

//TODO: Não vi necessidade de nesse primeiro momento fazer os metodos de PUT e DELETE, mas revisitar aqui caso haja necessidade
@RestController
@RequestMapping("votacao")
@Tag(name = "Votação", description = "Operações relacionadas as Votações em pautas abertas")
public class VotacaoController {
    @Autowired private VotacaoService votacaoService;
    @Autowired private PautaService pautaService;

    @GetMapping
    @Operation(summary = "Listar Votações com filtro",
            description = "Retorna uma consulta SQL com base nos critérios de filtro, paginação e ordenação.\n " +
                    "Caso não seja informado nenhum filtro, todos os registros serão retornados (considerando ou não a paginação e ordenação.")
    public ResponseEntity<?> listAllVotacoesWithFilter(
            @ModelAttribute FiltroVotacaoDTO filtroVotacaoDTO,
            Pageable pageable
    ) {
        return ResponseEntity.ok(votacaoService.findAllVotacoesWithFilter(filtroVotacaoDTO, pageable));
    }

    @PostMapping("/criar-sessao")
    @Operation(summary = "Abrir uma nova votação em alguma pautas existente",
            description = "Abre uma nova sessão para receber os votos em uma pauta existente.\n\n" +
                    "Caso não seja informado uma data específica ou prazo de duração para a votação,;será considerada o tempo default de 1 min.")
    public ResponseEntity<?> saveVotacao(@RequestBody @Valid SessaoVotacaoDTO dto) {
        return ResponseEntity.ok(votacaoService.createVotatingSession(dto));
    }

    @PostMapping("/votar")
    @Operation(summary = "Salvar um novo voto do associado na votação em aberto",
            description = "Salva o voto do associado caso seu cpf seja valido e se a votação estiver aberta para a pauta\n\n")
    public ResponseEntity<?> saveVoto(@RequestBody @Valid VotacaoDTO dto) {
        return ResponseEntity.ok(votacaoService.saveVoteInSession(dto));
    }

    @GetMapping("/votos")
    @Operation(summary = "Listar Votos dos associados com filtro",
            description = "Retorna uma consulta SQL com base nos critérios de filtro, paginação e ordenação.\n " +
                    "Caso não seja informado nenhum filtro, todos os registros serão retornados (considerando ou não a paginação e ordenação.")
    public ResponseEntity<?> listAllVotosWithFilter(
            @ModelAttribute FiltroVotoDTO filtroVotoDTO,
            Pageable pageable
    ) {
        return ResponseEntity.ok(votacaoService.findAllVotosWithFilter(filtroVotoDTO, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Votação pelo ID",
            description = "Retorna uma consulta SQL com o registro encontrado, em caso de sucesso.")
    public ResponseEntity<?> getVotacaoById(@PathVariable UUID id) {
        return ResponseEntity.ok(votacaoService.findVotacaoById(id));
    }

    @PostMapping("/votar-form")
    @Operation(summary = "Votação via formulário",
            description = "Salva votação através do formulário de votação.")
    public ResponseEntity<?> votarViaFormulario(VotacaoDTO dto) {
        try {
            votacaoService.saveVoteInSession(dto);
            return ResponseEntity.ok().build();
        } catch (VotacaoExceptions.TEVotacaoInvalidoException ev) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ev.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro inesperado ao votar."));
        }
    }

    @GetMapping("/lista-abertas")
    @Operation(summary = "Listagem das votações abertas",
            description = "Lista somente as votações abertas no momento da chamada.")
    @ResponseBody
    public List<VotacaoResumoListasDTO> listarVotacoesAbertas(Pageable pageable) {
        FiltroVotacaoDTO filtro = new FiltroVotacaoDTO();
        filtro.setStatus(VotacaoStatus.ABERTA.name());
        return votacaoService.getListasResultadosSimples(filtro, pageable);
    }

    @GetMapping("/lista-encerradas")
    @Operation(summary = "Listagem das votações encerradas",
            description = "Lista somente as votações encerradas no momento da chamada.")
    @ResponseBody
    public List<VotacaoResumoListasDTO> listarVotacoesEncerradas(Pageable pageable) {
        FiltroVotacaoDTO filtro = new FiltroVotacaoDTO();
        filtro.setStatus(VotacaoStatus.ENCERRADA.name());
        return votacaoService.getListasResultadosSimples(filtro, pageable);
    }

}