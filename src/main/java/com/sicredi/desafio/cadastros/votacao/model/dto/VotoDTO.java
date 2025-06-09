package com.sicredi.desafio.cadastros.votacao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO {
    private String codigoVotacao;
    private String codigoAssociado; // TODO: Como tbm é único na tabela, estou capturando o codigo ao invés do id
    private String voto;
}
