package com.sicredi.desafio.cadastros.votacao.scheduler.filas;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
// msg enviada para a fila com a duracao (quando ela deve ser encerrada) - fila d votacao
public class SessaoVotacaoMessage implements Serializable {
    private static final long serialVersionUID = 1L; // id único-Serialização
    private UUID idVotacao;
    private LocalDateTime dataHoraEncerramento;

    public SessaoVotacaoMessage() {}//(deserialização) por causa do Jackson/RabbitMQ.

    public SessaoVotacaoMessage(UUID idVotacao, LocalDateTime dataHoraEncerramento) {
        this.idVotacao = idVotacao;
        this.dataHoraEncerramento = dataHoraEncerramento;
    }
}
