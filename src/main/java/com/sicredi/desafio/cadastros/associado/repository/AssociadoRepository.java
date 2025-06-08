package com.sicredi.desafio.cadastros.associado.repository;

import com.sicredi.desafio.cadastros.associado.model.entity.Associado;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, UUID> {
    @Query("SELECT MAX(c.codigo) FROM Associado c")
    Integer findLastCode();

    @NonNull
    Optional<Associado> findByCodigo(@NonNull String codigo);

}
