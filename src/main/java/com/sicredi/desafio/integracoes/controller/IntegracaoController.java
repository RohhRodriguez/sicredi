package com.sicredi.desafio.integracoes.controller;

import com.sicredi.desafio.integracoes.service.IntegracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO: fiz um controller para caso em algum momento o front queira consultar o cpf antes de aprovar
// o cadastro por exemplo, poderia chamar esse endpoint
@RestController
@RequestMapping("integracao") //https://user-info.herokuapp.com/users/{cpf}
@Tag(name = "Consulta de CPF", description = "Operações relacionadas às Assembléias")
public class IntegracaoController {
    @Autowired private IntegracaoService integracaoService;

    @GetMapping("/{cpf}")
    @Operation(summary = "Verifica se um cpf é válido",
            description = "Consulta externa se o CPF informado é válido.")
    public ResponseEntity<?> validaCPF(@PathVariable String cpf) {
        return ResponseEntity.ok(integracaoService.validaCPF(cpf));
    }

}