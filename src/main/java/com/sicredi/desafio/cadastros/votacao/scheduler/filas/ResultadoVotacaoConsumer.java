package com.sicredi.desafio.cadastros.votacao.scheduler.filas;

import com.sicredi.desafio.cadastros.email.EmailService;
import com.sicredi.desafio.config.FilaConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
public class ResultadoVotacaoConsumer {
    @Autowired private EmailService emailService;

    @RabbitListener(queues = FilaConfig.RESULTADO_VOTACAO_QUEUE)
    public void consumir(ResultadoVotacaoMessage message) {
        System.out.println("Resultado recebido da fila: " + message);

        //calcula a duração da votação
        Duration duracao = Duration.between(message.getDataHoraInicio(), message.getDataHoraEncerramento());
        LocalTime duracaoFormatada = LocalTime.MIDNIGHT.plus(duracao); // hh:mm:ss

        // qtde e % se não estiver preenchidos
        int totalVotos = message.getQuantidadeVotos() != null ? message.getQuantidadeVotos() : 0;
        double percentualSim = message.getPercentual() != null ? message.getPercentual() : 0.0;
        int votosSim = (int) Math.round((percentualSim/100.0)*totalVotos);
        int votosNao = totalVotos - votosSim;

        // e-mail com o result
        String corpoEmail = String.format("""
        **Resultado da Votação**

        ID Sessão: %s
        Código: %s
        Assembleia: %s
        Pauta: %s
        Duração: %s
        Início: %s
        Encerramento: %s
        Total de Votos: %d
        Votos SIM: %d
        Votos NÃO: %d
        Resultado: %s
        Percentual de Aprovação: %.2f%%
        Status: %s
        """,
                message.getIdSessao(),
                message.getCodigo(),
                message.getCodigoAssembleia(),
                message.getCodigoPauta(),
                duracaoFormatada,
                message.getDataHoraInicio(),
                message.getDataHoraEncerramento(),
                totalVotos,
                votosSim,
                votosNao,
                message.getResultado(),
                percentualSim,
                message.getStatus()
        );
        emailService.enviarResultado(message, false, "rohh3578@gmail.com", "Resultado da Votação", corpoEmail);//envia o e-mail
    }
}
