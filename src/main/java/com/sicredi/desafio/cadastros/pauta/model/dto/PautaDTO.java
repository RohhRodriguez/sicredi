package com.sicredi.desafio.cadastros.pauta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PautaDTO {
    private String nome;
    private String descricao;
    private String codigoAssembleia;
}
