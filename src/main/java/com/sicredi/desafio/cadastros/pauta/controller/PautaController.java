package com.sicredi.desafio.cadastros.pauta.controller;

import com.sicredi.desafio.cadastros.pauta.model.dto.FiltroPautaDTO;
import com.sicredi.desafio.cadastros.pauta.model.dto.PautaDTO;
import com.sicredi.desafio.cadastros.pauta.service.PautaService;
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
@RequestMapping("pauta")
@Tag(name = "Pauta", description = "Operações relacionadas as Pautas de votação")
public class PautaController {
    @Autowired
    private PautaService pautaService;

    @GetMapping
    @Operation(summary = "Listar Pautas com filtro",
            description = "Retorna uma consulta SQL com base nos critérios de filtro, paginação e ordenação.\n " +
                    "Caso não seja informado nenhum filtro, todos os registros serão retornados (considerando ou não a paginação e ordenação.")
    public ResponseEntity<?> listAllPautasWithFilter(
            @ModelAttribute FiltroPautaDTO filtroPautasDTO,
            Pageable pageable
    ) {
        return ResponseEntity.ok(pautaService.findAllPautasWithFilter(filtroPautasDTO, pageable));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova Pauta para votação",
            description = "Cadastra uma nova Pauta para votação e retorna suas informações em caso de sucesso\n\n")
    public ResponseEntity<?> savePauta(@RequestBody @Valid PautaDTO dto) {
        return ResponseEntity.ok(pautaService.savePauta(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Pauta pelo ID",
            description = "Retorna uma consulta SQL com o registro encontrado, em caso de sucesso.")
    public ResponseEntity<?> getPautaById(@PathVariable UUID id) {
        return ResponseEntity.ok(pautaService.findPautaById(id));
    }

}