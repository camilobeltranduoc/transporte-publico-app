package com.transporte.gps.service;

import com.transporte.gps.config.RabbitMQConfig;
import com.transporte.gps.dto.GpsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GpsProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void enviarGps(GpsMessage gpsMessage) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            gpsMessage
        );
    }
}
