package com.sicredi.desafio.cadastros.pauta.repository;

import com.sicredi.desafio.cadastros.pauta.model.entity.Pauta;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {
    @Query("SELECT MAX(c.codigo) FROM Pauta c")
    Integer findLastCode();

    @NonNull
    Optional<Pauta> findByCodigo(@NonNull String codigo);

    List<Pauta> findAllByCodigoAssembleia(String codigoAssembleia);
}
