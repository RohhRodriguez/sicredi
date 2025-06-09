package com.sicredi.desafio.cadastros.votacao.repository;

import com.sicredi.desafio.cadastros.votacao.model.entity.Voto;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {
    @Query("SELECT MAX(c.codigo) FROM Voto c")
    Integer findLastCode();

    @NonNull
    Optional<Voto> findByCodigo(@NonNull String codigo);

    List<Voto> findAllByCodigoVotacao(String codigoVotacao);

    //TODO: estou criando esse metodo para verificar se já tem algum voto salvo para a mesma votacao aberta (votacao já tem a pauta associada)
    Optional<Voto> findByCodigoVotacaoAndCodigoAssociado(String codigoVotacao, String codigoAssociado);

}
