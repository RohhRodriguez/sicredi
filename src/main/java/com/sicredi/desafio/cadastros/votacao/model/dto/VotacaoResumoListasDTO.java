package com.sicredi.desafio.cadastros.votacao.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import com.sicredi.desafio.cadastros.pauta.model.entity.Pauta;
import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VotacaoResumoListasDTO {
    private String codigo;
    private String codigoAssembleia;
    private String descricaoAssembleia;
    private String codigoPauta;
    private String descricaoPauta;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHoraInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHoraEncerramento;
    private Integer votosSim;
    private Integer votosNao;
    private Integer quantidadeVotos;
    private Double percentual;
    private String resultado;

    public VotacaoResumoListasDTO(Votacao v, Assembleia a, Pauta p) {
        this.codigo = v.getCodigo();
        this.codigoAssembleia = a.getCodigo();
        this.descricaoAssembleia = a.getDescricao();
        this.codigoPauta = p.getCodigo();
        this.descricaoPauta = p.getDescricao();
        this.status = v.getStatus();
        this.dataHoraInicio = v.getDataHoraInicio();
        this.dataHoraEncerramento = v.getDataHoraEncerramento();
        this.quantidadeVotos = v.getQuantidadeVotos();
        this.percentual = v.getPercentual();
        this.resultado = v.getResultado();
    }

}
