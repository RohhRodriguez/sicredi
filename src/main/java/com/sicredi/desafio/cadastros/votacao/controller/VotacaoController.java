package com.sicredi.desafio.cadastros.votacao.controller;

import com.sicredi.desafio.cadastros.votacao.model.dto.FiltroVotacaoDTO;
import com.sicredi.desafio.cadastros.votacao.model.dto.FiltroVotoDTO;
import com.sicredi.desafio.cadastros.votacao.model.dto.SessaoVotacaoDTO;
import com.sicredi.desafio.cadastros.votacao.model.dto.VotacaoDTO;
import com.sicredi.desafio.cadastros.votacao.service.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//TODO: Não vi necessidade de nesse primeiro momento fazer os metodos de PUT e DELETE, mas revisitar aqui caso haja necessidade
@RestController
@RequestMapping("votacao")
@Tag(name = "Votação", description = "Operações relacionadas as Votações em pautas abertas")
public class VotacaoController {
    @Autowired
    private VotacaoService votacaoService;

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

}