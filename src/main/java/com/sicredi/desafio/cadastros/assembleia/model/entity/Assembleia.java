package com.sicredi.desafio.cadastros.assembleia.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sicredi.desafio.utils.UtilLogAuditoria;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "assembleia", schema = "sicredi")
public class Assembleia extends UtilLogAuditoria implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "quantidade_membros")
    private Integer quantidadeMembros;

    @Column(name = "status", nullable = false)
    private String status;

    @JsonIgnore
    public LocalDateTime getDataHoraInclusao() {
        return this.dataHoraInclusao;
    }
}
