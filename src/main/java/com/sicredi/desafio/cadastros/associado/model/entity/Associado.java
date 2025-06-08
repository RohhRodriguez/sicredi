package com.sicredi.desafio.cadastros.associado.model.entity;

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
@Table(name = "associado", schema = "sicredi")
public class Associado extends UtilLogAuditoria implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name = "status", nullable = false)
    private String status;

    @JsonIgnore
    public LocalDateTime getDataHoraInclusao() {
        return this.dataHoraInclusao;
    }
}

