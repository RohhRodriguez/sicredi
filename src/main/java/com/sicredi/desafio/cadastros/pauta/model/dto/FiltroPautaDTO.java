package com.sicredi.desafio.cadastros.pauta.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FiltroPautaDTO {
    private String codigo;
    private String nome;
    private String descricao;
    private String codigoAssembleia;
    private List<String> statusList;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataInicialInclusao;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataFinalInclusao;

}
