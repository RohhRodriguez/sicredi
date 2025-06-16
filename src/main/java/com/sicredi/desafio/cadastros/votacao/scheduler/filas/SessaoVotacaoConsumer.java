package com.sicredi.desafio.cadastros.votacao.scheduler.filas;

import com.sicredi.desafio.cadastros.votacao.model.entity.Votacao;
import com.sicredi.desafio.cadastros.votacao.service.VotacaoService;
import com.sicredi.desafio.config.FilaConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

//consumer de votação
@Component
public class SessaoVotacaoConsumer {
    @Autowired private VotacaoService votacaoService;
    @Autowired private RabbitTemplate rabbitTemplate;

    //escuta a fila de votacoes
    @RabbitListener(queues = FilaConfig.SESSOES_VOTACAO_QUEUE)
    public void processarSessao(SessaoVotacaoMessage message) {
        System.out.println("entrou aqui");
        System.out.println("Mensagem recebida na fila: " + message.getIdVotacao());

        // Calcula o tempo até acabar votação
        long millisToWait = Duration.between(LocalDateTime.now(), message.getDataHoraEncerramento()).toMillis();
        if (millisToWait > 0) {
            System.out.println("Aguardando " + millisToWait + "ms até o encerramento da sessão...");
            try {
                Thread.sleep(millisToWait); // Espera ativa (não recomendado em produção)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrompida ao aguardar encerramento da votação: " + message.getIdVotacao());
                return;
            }
        }
        // encerra
        Votacao votacaoEncerrada = votacaoService.encerraVotacao(message.getIdVotacao());
        ResultadoVotacaoMessage resultadoMessage = montarResultadoVotacaoMessage(votacaoEncerrada);

        // Envia o resultado para a fila de resultso
        rabbitTemplate.convertAndSend(
                "votacao-exchange",
                FilaConfig.RESULTADO_VOTACAO_QUEUE,
                resultadoMessage
        );
        //quartzJobScheduler.agendarEncerramentoVotacao(votacaoEncerrada.getId(), votacaoEncerrada.getDataHoraEncerramento());
        System.out.println("Resultado enviado para fila de resultado-votacao: " + resultadoMessage.getIdSessao());
    }

    private static ResultadoVotacaoMessage montarResultadoVotacaoMessage(Votacao votacaoEncerrada) {
        ResultadoVotacaoMessage resultadoMessage = new ResultadoVotacaoMessage();

        resultadoMessage.setIdSessao(votacaoEncerrada.getId());
        resultadoMessage.setCodigo(votacaoEncerrada.getCodigo());
        resultadoMessage.setCodigoAssembleia(votacaoEncerrada.getCodigoAssembleia());
        resultadoMessage.setCodigoPauta(votacaoEncerrada.getCodigoPauta());
        resultadoMessage.setDuracao(votacaoEncerrada.getDuracao());
        resultadoMessage.setDataHoraInicio(votacaoEncerrada.getDataHoraInicio());
        resultadoMessage.setDataHoraEncerramento(votacaoEncerrada.getDataHoraEncerramento());
        resultadoMessage.setQuantidadeVotos(votacaoEncerrada.getQuantidadeVotos());
        resultadoMessage.setResultado(votacaoEncerrada.getResultado());
        resultadoMessage.setPercentual(votacaoEncerrada.getPercentual());
        resultadoMessage.setStatus(votacaoEncerrada.getStatus());

        return resultadoMessage;
    }
}
