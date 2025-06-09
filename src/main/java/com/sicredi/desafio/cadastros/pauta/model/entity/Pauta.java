package com.sicredi.desafio.cadastros.pauta.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sicredi.desafio.utils.UtilLogAuditoria;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "assembleia_pauta", schema = "sicredi")
public class Pauta extends UtilLogAuditoria implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "codigo_assembleia", nullable = false)
    private String codigoAssembleia;

    @Column(name = "nome", nullable = false)
    private String nome;

    //TODO: estou deixando como opcional, mas se fosse obrigatório incluir ao menos uma pequena descrição, poderia ser setado como obrigatório tbm
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status", nullable = false)
    private String status;

    @JsonIgnore
    public LocalDateTime getDataHoraInclusao() {
        return this.dataHoraInclusao;
    }
}

