package com.transporte.kafka.consumidor.gps.service;

import com.transporte.kafka.consumidor.gps.dto.UbicacionMessage;
import com.transporte.kafka.consumidor.gps.entity.KafkaHistorialGps;
import com.transporte.kafka.consumidor.gps.repository.KafkaHistorialGpsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class GpsConsumerService {

    private final KafkaHistorialGpsRepository repository;

    @KafkaListener(topics = "${kafka.topics.ubicaciones}", groupId = "consumidor-gps-group")
    public void consumirUbicacion(UbicacionMessage mensaje) {
        log.info("Ubicacion recibida -> vehiculo={} parada={}",
                mensaje.getIdVehiculo(), mensaje.getNombreParada());

        KafkaHistorialGps historial = new KafkaHistorialGps();
        historial.setIdVehiculo(mensaje.getIdVehiculo());
        historial.setLatitud(mensaje.getLatitud());
        historial.setLongitud(mensaje.getLongitud());
        historial.setTimestamp(mensaje.getTimestamp());
        historial.setNombreParada(mensaje.getNombreParada());
        historial.setFechaRegistro(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        repository.save(historial);
        log.info("Ubicacion guardada en Oracle -> id={}", historial.getId());
    }
}
