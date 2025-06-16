package com.sicredi.desafio.cadastros.pauta.model.dto;

import com.sicredi.desafio.cadastros.votacao.model.dto.VotacaoResumoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaComVotacoesDTO {
    private String codigo;
    private String nome;
    private List<VotacaoResumoDTO> votacoes;
}
