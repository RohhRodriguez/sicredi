package com.sicredi.desafio.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class UtilLogAuditoria {

    @JsonIgnore
    @Column(name = "data_hora_inclusao")
    public LocalDateTime dataHoraInclusao;

    @JsonIgnore
    @Column(name = "usuario_inclusao")
    public String usuarioInclusao;

    @JsonIgnore
    @Column(name = "data_hora_alteracao")
    public LocalDateTime dataHoraAlteracao;

    @JsonIgnore
    @Column(name = "usuario_alteracao")
    public String usuarioAlteracao;

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
        this.usuarioInclusao = getLoggedUser();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataHoraAlteracao = LocalDateTime.now();
        this.usuarioAlteracao = getLoggedUser();
    }

    @JsonIgnore
    public String getLoggedUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "system";
        }
    }
}
