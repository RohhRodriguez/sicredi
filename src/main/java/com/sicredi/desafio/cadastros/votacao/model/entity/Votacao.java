package com.sicredi.desafio.cadastros.votacao.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sicredi.desafio.utils.UtilLogAuditoria;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "assembleia_pauta_votacao", schema = "sicredi")
public class Votacao extends UtilLogAuditoria implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "codigo_assembleia", nullable = false)
    private String codigoAssembleia;

    @Column(name = "codigo_pauta", nullable = false)
    private String codigoPauta;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "duracao")
    private LocalTime duracao;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "data_hora_encerramento")
    private LocalDateTime dataHoraEncerramento;

    @Column(name="total_votos")
    private Integer quantidadeVotos;

    @Column(name="resultado")
    private String resultado;

    @Column(name="percentual_votos")
    private Double percentual;

    @Column(name="status")
    private String status;

    @JsonIgnore
    public LocalDateTime getDataHoraInclusao() {
        return this.dataHoraInclusao;
    }
}

