package com.sicredi.desafio.cadastros.votacao.model.dto;

import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import com.sicredi.desafio.cadastros.associado.model.entity.Associado;
import com.sicredi.desafio.cadastros.pauta.model.entity.Pauta;
import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;

public record VotacaoInfoCompletaDTO (
        Associado associado,
        Assembleia assembleia,
        Pauta pauta,
        Votacao votacao
){}
