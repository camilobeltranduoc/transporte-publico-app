package com.transporte.kafka.productor.service;

import com.transporte.kafka.productor.dto.UbicacionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UbicacionProducerService {

    private final KafkaTemplate<String, UbicacionMessage> kafkaTemplate;

    @Value("${kafka.topics.ubicaciones}")
    private String topicUbicaciones;

    private final Random random = new Random();

    private static final List<String[]> PARADAS = List.of(
            new String[]{"Parada Central",      "-33.4567", "-70.6789"},
            new String[]{"Parada Norte",         "-33.4012", "-70.6543"},
            new String[]{"Parada Sur",           "-33.5102", "-70.6901"},
            new String[]{"Parada Oriente",       "-33.4489", "-70.6234"},
            new String[]{"Parada Poniente",      "-33.4601", "-70.7102"},
            new String[]{"Terminal Principal",   "-33.4400", "-70.6600"}
    );

    // Cron que se ejecuta cada 1 segundo (requerimiento del rubric)
    @Scheduled(fixedRate = 1000)
    public void publicarUbicacionAutomatica() {
        long idVehiculo = (random.nextInt(5) + 1);
        String[] parada = PARADAS.get(random.nextInt(PARADAS.size()));

        UbicacionMessage mensaje = new UbicacionMessage(
                idVehiculo,
                Double.parseDouble(parada[1]) + (random.nextDouble() * 0.01 - 0.005),
                Double.parseDouble(parada[2]) + (random.nextDouble() * 0.01 - 0.005),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                parada[0]
        );

        kafkaTemplate.send(topicUbicaciones, String.valueOf(idVehiculo), mensaje);
        log.info("Ubicacion publicada -> vehiculo={} parada={}", idVehiculo, parada[0]);
    }

    public void publicarUbicacion(UbicacionMessage mensaje) {
        if (mensaje.getTimestamp() == null) {
            mensaje.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        kafkaTemplate.send(topicUbicaciones, String.valueOf(mensaje.getIdVehiculo()), mensaje);
        log.info("Ubicacion publicada manualmente -> vehiculo={}", mensaje.getIdVehiculo());
    }
}
