package com.sicredi.desafio.cadastros.associado.controller;

import com.sicredi.desafio.cadastros.associado.model.dto.AssociadoDTO;
import com.sicredi.desafio.cadastros.associado.model.dto.FiltroAssociadoDTO;
import com.sicredi.desafio.cadastros.associado.service.AssociadoService;
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
@RequestMapping("associado")
@Tag(name = "Associado", description = "Operações relacionadas aos associados Sicredi")
public class AssociadoController {
    @Autowired
    private AssociadoService associadoService;

    @GetMapping
    @Operation(summary = "Listar Associados com filtro",
            description = "Retorna uma consulta SQL com base nos critérios de filtro, paginação e ordenação.")
    public ResponseEntity<?> listAllAssociadoWithFilter(
            @ModelAttribute FiltroAssociadoDTO filtroAssociadoDTO,
            Pageable pageable
    ) {
        return ResponseEntity.ok(associadoService.findAllAssociadosWithFilter(filtroAssociadoDTO, pageable));
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo Associado",
            description = "Cadastra um novo associado e retorna os dados salvos. Caso sucesso - Status: Ativo\n\n")
    public ResponseEntity<?> saveAssociado(@RequestBody @Valid AssociadoDTO dto) {
        return ResponseEntity.ok(associadoService.saveAssociado(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Associado pelo ID",
            description = "Retorna uma consulta SQL com o registro encontrado, em caso de sucesso.")
    public ResponseEntity<?> getAssociadoById(@PathVariable UUID id) {
        return ResponseEntity.ok(associadoService.findAssociadoById(id));
    }

}