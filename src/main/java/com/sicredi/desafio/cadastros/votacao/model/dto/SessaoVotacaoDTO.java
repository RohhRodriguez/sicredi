package com.sicredi.desafio.cadastros.votacao.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessaoVotacaoDTO {
    private String codigoPauta;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHoraInicio;

    private DuracaoDTO duracao;

    @JsonIgnore
    public Duration getDuracaoConvertida() {
        if (duracao == null) {
            return Duration.ZERO;
        }
        return Duration.ofHours(duracao.getHoras())
                .plusMinutes(duracao.getMinutos())
                .plusSeconds(duracao.getSegundos());
    }



}
