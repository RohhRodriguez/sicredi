package com.sicredi.desafio.cadastros.votacao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotacaoDTO {
    private String codigoPauta;
    private String codigoAssociado;
    private String voto; //TODO: apenas sim ou nao - criar enum
}
