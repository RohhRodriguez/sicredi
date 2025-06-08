package com.sicredi.desafio.cadastros.assembleia.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssembleiaDTO {
    private String nome;
    private String descricao;
    private Integer quantidadeMembros;
}
