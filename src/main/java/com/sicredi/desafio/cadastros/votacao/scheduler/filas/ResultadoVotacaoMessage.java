package com.sicredi.desafio.cadastros.votacao.scheduler.filas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoVotacaoMessage implements Serializable {
    private UUID idSessao;
    private String codigo;
    private String codigoAssembleia;
    private String codigoPauta;
    private LocalTime duracao;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraEncerramento;
    private Integer quantidadeVotos;
    private String resultado;
    private Double percentual;
    private String status;
}
