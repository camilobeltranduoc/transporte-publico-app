package com.transporte.kafka.procesamiento.service;

import com.transporte.kafka.procesamiento.dto.HorarioMessage;
import com.transporte.kafka.procesamiento.dto.UbicacionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcesamientoService {

    private final KafkaTemplate<String, HorarioMessage> kafkaTemplate;

    @Value("${kafka.topics.horarios}")
    private String topicHorarios;

    @KafkaListener(topics = "${kafka.topics.ubicaciones}", groupId = "procesamiento-group")
    public void procesarUbicacion(UbicacionMessage ubicacion) {
        log.info("Procesando ubicacion -> vehiculo={} parada={}",
                ubicacion.getIdVehiculo(), ubicacion.getNombreParada());

        HorarioMessage horario = calcularHorario(ubicacion);
        kafkaTemplate.send(topicHorarios, String.valueOf(horario.getIdVehiculo()), horario);

        log.info("Horario publicado -> vehiculo={} parada={} llegada={}",
                horario.getIdVehiculo(), horario.getNombreParada(), horario.getHorarioLlegada());
    }

    private HorarioMessage calcularHorario(UbicacionMessage ubicacion) {
        // Lógica de cálculo de horario estimado basado en la ubicación
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime llegadaEstimada = ahora.plusMinutes(2 + (long)(Math.random() * 8));

        String estado = determinarEstado(ubicacion.getNombreParada());

        return new HorarioMessage(
                ubicacion.getIdVehiculo(),
                ubicacion.getNombreParada(),
                llegadaEstimada.format(DateTimeFormatter.ofPattern("HH:mm")),
                ahora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                estado
        );
    }

    private String determinarEstado(String parada) {
        if (parada != null && parada.contains("Terminal")) {
            return "EN_TERMINAL";
        }
        return "EN_RUTA";
    }
}
