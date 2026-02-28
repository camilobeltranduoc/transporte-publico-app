package com.transporte.kafka.consumidor.horarios.service;

import com.transporte.kafka.consumidor.horarios.dto.HorarioMessage;
import com.transporte.kafka.consumidor.horarios.entity.KafkaHistorialHorario;
import com.transporte.kafka.consumidor.horarios.repository.KafkaHistorialHorarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class HorarioConsumerService {

    private final KafkaHistorialHorarioRepository repository;

    @KafkaListener(topics = "${kafka.topics.horarios}", groupId = "consumidor-horarios-group")
    public void consumirHorario(HorarioMessage mensaje) {
        log.info("Horario recibido -> vehiculo={} parada={} llegada={}",
                mensaje.getIdVehiculo(), mensaje.getNombreParada(), mensaje.getHorarioLlegada());

        KafkaHistorialHorario historial = new KafkaHistorialHorario();
        historial.setIdVehiculo(mensaje.getIdVehiculo());
        historial.setNombreParada(mensaje.getNombreParada());
        historial.setHorarioLlegada(mensaje.getHorarioLlegada());
        historial.setTimestamp(mensaje.getTimestamp());
        historial.setEstado(mensaje.getEstado());
        historial.setFechaRegistro(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        repository.save(historial);
        log.info("Horario guardado en Oracle -> id={}", historial.getId());
    }
}
