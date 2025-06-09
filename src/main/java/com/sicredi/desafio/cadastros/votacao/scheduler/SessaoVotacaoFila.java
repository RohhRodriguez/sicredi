package com.sicredi.desafio.cadastros.votacao.scheduler;

import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//TODO: Substituir fila manual por mensageria/fila externa depois de finalizar a primeira parte (melhoriaa)
@Component
public class SessaoVotacaoFila {
    private final Queue<Votacao> sessoesAbertas = new ConcurrentLinkedQueue<>();

    public void adicionarSessao(Votacao votacao) {
        sessoesAbertas.add(votacao);
    }

    public void removerSessao(Votacao votacao) {
        sessoesAbertas.remove(votacao);
    }

    public List<Votacao> listarTodas() {
        return new ArrayList<>(sessoesAbertas);
    }
}
