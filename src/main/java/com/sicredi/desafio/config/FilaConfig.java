package com.sicredi.desafio.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class FilaConfig {
    public static final String SESSOES_VOTACAO_QUEUE = "sessoes-votacao";
    public static final String RESULTADO_VOTACAO_QUEUE = "resultado-votacao";

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        return new SchedulerFactoryBean();
    }

    @Bean
    public Queue sessoesVotacaoQueue() {
        return QueueBuilder.durable(SESSOES_VOTACAO_QUEUE).build();//cria a fila de votação
    }

    @Bean
    public Queue resultadoVotacaoQueue() {
        return QueueBuilder.durable(RESULTADO_VOTACAO_QUEUE).build();//cria a fila dos resultados
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("votacao-exchange");// manda cada msg pra sua fila
    }

    @Bean
    public Binding bindingSessoesVotacao(Queue sessoesVotacaoQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(sessoesVotacaoQueue)
                .to(exchange)
                .with(SESSOES_VOTACAO_QUEUE); //fila com votacoes abertas
    }

    @Bean
    public Binding bindingResultadoVotacao(Queue resultadoVotacaoQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(resultadoVotacaoQueue)
                .to(exchange)
                .with(RESULTADO_VOTACAO_QUEUE);//quando acaba a votação a msg vem pra cá
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
