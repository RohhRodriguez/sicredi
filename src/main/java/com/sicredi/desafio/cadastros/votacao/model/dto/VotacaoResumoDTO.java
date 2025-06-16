package com.sicredi.desafio.cadastros.votacao.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotacaoResumoDTO {
    private String codigo;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHoraInicio;
}
