package com.transporte.consumidor.gps.service;

import com.transporte.consumidor.gps.dto.GpsMessage;
import com.transporte.consumidor.gps.entity.HistorialGps;
import com.transporte.consumidor.gps.repository.HistorialGpsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GpsConsumerService {

    private final HistorialGpsRepository historialGpsRepository;

    @RabbitListener(queues = "q.transporte.gps")
    public void consumirGps(GpsMessage gpsMessage) {
        HistorialGps historial = new HistorialGps();
        historial.setIdVehiculo(gpsMessage.getIdVehiculo());
        historial.setLatitud(gpsMessage.getLatitud());
        historial.setLongitud(gpsMessage.getLongitud());
        historial.setTimestamp(gpsMessage.getTimestamp());
        historialGpsRepository.save(historial);
    }
}
