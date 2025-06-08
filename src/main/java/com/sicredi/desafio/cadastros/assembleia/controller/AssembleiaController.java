package com.sicredi.desafio.cadastros.assembleia.controller;

import com.sicredi.desafio.cadastros.assembleia.model.dto.AssembleiaDTO;
import com.sicredi.desafio.cadastros.assembleia.model.dto.FiltroAssembleiaDTO;
import com.sicredi.desafio.cadastros.assembleia.repository.AssembleiaRepository;
import com.sicredi.desafio.cadastros.assembleia.service.AssembleiaService;
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
@RequestMapping("assembleia")
@Tag(name = "Assembleia", description = "Operações relacionadas às Assembléias")
public class AssembleiaController {
    @Autowired private AssembleiaService assembleiaService;
    @Autowired private AssembleiaRepository assembleiaRepository;

    @GetMapping
    @Operation(summary = "Listar Assembléias com filtro",
            description = "Retorna uma consulta SQL com base nos critérios de filtro, paginação e ordenação.")
    public ResponseEntity<?> listAllAssembleiaWithFilter(
            @ModelAttribute FiltroAssembleiaDTO filtroAssembleiaDTO,
            Pageable pageable
    ) {
        return ResponseEntity.ok(assembleiaService.findAllAssembleiasWithFilter(filtroAssembleiaDTO, pageable));
    }

    @PostMapping
    @Operation(summary = "Cadastrar um nova Assembleia",
            description = "Cadastra um nova Assembleia e retorna os dados salvos. Caso sucesso - Status: Ativo\n\n")
    public ResponseEntity<?> saveAssembleia(@RequestBody @Valid AssembleiaDTO dto) {
        return ResponseEntity.ok(assembleiaService.saveAssembleia(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Assembléia pelo ID",
            description = "Retorna uma consulta SQL com o registro encontrado, em caso de sucesso.")
    public ResponseEntity<?> getAssembleiaById(@PathVariable UUID id) {
        return ResponseEntity.ok(assembleiaService.findAssembleiaById(id));
    }

}