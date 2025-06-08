package com.sicredi.desafio.cadastros.assembleia.repository;


import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssembleiaRepository extends JpaRepository<Assembleia, UUID> {
    @Query("SELECT MAX(c.codigo) FROM Assembleia c")
    Integer findLastCode();

    @NonNull
    Optional<Assembleia> findByCodigo(@NonNull String codigo);
}
