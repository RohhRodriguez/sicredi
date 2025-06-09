package com.sicredi.desafio.cadastros.votacao.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sicredi.desafio.utils.UtilLogAuditoria;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assembleia_pauta_votacao_itens", schema = "sicredi")
public class Voto extends UtilLogAuditoria implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "codigo_votacao", nullable = false)
    private String codigoVotacao;

    @Column(name = "codigo_associado", nullable = false)
    private String codigoAssociado;

    @Column(name = "voto", nullable = false)
    private String voto;

    @JsonIgnore
    public LocalDateTime getDataHoraInclusao() {
        return this.dataHoraInclusao;
    }
}