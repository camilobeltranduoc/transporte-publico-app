package com.transporte.consumidor.horarios.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transporte.consumidor.horarios.dto.HorarioMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HorarioConsumerService {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @RabbitListener(queues = "q.transporte.horarios")
    public void consumirHorario(HorarioMessage message) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(message);
        String key = "horarios/ruta-" + message.getIdRuta() + "-" + UUID.randomUUID() + ".json";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/json")
                .build();

        s3Client.putObject(request, RequestBody.fromString(payload, StandardCharsets.UTF_8));
    }
}
