package com.transporte.horarios.service;

import com.transporte.horarios.config.RabbitMQConfig;
import com.transporte.horarios.dto.HorarioMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HorarioProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void enviarHorario(HorarioMessage horarioMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            horarioMessage
        );
    }
}
