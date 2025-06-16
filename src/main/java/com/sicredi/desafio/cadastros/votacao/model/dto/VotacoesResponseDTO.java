package com.sicredi.desafio.cadastros.votacao.model.dto;

import java.util.List;

public class VotacoesResponseDTO {
    private List<VotacaoResumoListasDTO> abertas;
    private List<VotacaoResumoListasDTO> encerradas;

    public VotacoesResponseDTO(List<VotacaoResumoListasDTO> abertas, List<VotacaoResumoListasDTO> encerradas) {
        this.abertas = abertas;
        this.encerradas = encerradas;
    }

    public List<VotacaoResumoListasDTO> getAbertas() {
        return abertas;
    }

    public List<VotacaoResumoListasDTO> getEncerradas() {
        return encerradas;
    }
}

