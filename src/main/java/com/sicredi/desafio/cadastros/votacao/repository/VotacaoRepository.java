package com.sicredi.desafio.cadastros.votacao.repository;

import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, UUID> {
    @Query("SELECT MAX(c.codigo) FROM Votacao c")
    Integer findLastCode();

    @NonNull
    Optional<Votacao> findByCodigo(@NonNull String codigo);

    List<Votacao> findAllByCodigoPauta(String codigoPauta);

    @Query("SELECT v FROM Votacao v WHERE v.codigoPauta = :codigoPauta AND v.status = 'ABERTA' ORDER BY v.dataHoraInicio DESC")
    List<Votacao> findVotacoesAbertasByCodigoPauta(String codigoPauta);

}
