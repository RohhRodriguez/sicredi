package com.sicredi.desafio.cadastros.votacao.scheduler.filasmanuais;

import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import com.sicredi.desafio.cadastros.votacao.repository.VotacaoRepository;
import com.sicredi.desafio.cadastros.votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessaoVotacaoScheduler {

    @Autowired private SessaoVotacaoFila fila;
    @Autowired private VotacaoRepository votacaoRepository;
    @Autowired private VotacaoService votacaoService;

    @Scheduled(fixedRate = 5_000) // 5 segundos TODO: verificar se rodar a cada 5 seguntos é suficiente...
    public void verificarSessoes() {
        LocalDateTime agora = LocalDateTime.now();
        for (Votacao votacao : fila.listarTodas()) {
            if (votacao.getDataHoraEncerramento().isBefore(agora)) {

                //chamo aqui o método da service que já calcula o resumo
                votacaoService.encerraVotacao(votacao.getId());

                fila.removerSessao(votacao); // Remove da fila
                System.out.println("Sessão encerrada: " + votacao.getCodigo());
            }
        }
    }
}
