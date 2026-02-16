package com.transporte.consumidor.horarios.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "q.transporte.horarios";
    public static final String EXCHANGE_NAME = "x.transporte";
    public static final String ROUTING_KEY = "horarios.#";

    @Bean
    public Queue horariosQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange horariosExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding horariosBinding(Queue horariosQueue, TopicExchange horariosExchange) {
        return BindingBuilder.bind(horariosQueue).to(horariosExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}
