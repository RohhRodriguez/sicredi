package com.sicredi.desafio.cadastros.associado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssociadoDTO {
    private String email;
    private String nome;
    private String cpf;
}
