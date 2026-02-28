package com.transporte.kafka.consumidor.gps.controller;

import com.transporte.kafka.consumidor.gps.entity.KafkaHistorialGps;
import com.transporte.kafka.consumidor.gps.repository.KafkaHistorialGpsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-gps")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GpsHistorialController {

    private final KafkaHistorialGpsRepository repository;

    @GetMapping
    public ResponseEntity<List<KafkaHistorialGps>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/vehiculo/{id}")
    public ResponseEntity<List<KafkaHistorialGps>> getByVehiculo(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findByIdVehiculo(id));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ms-kafka-consumidor-gps UP - escuchando ubicaciones_vehiculos");
    }
}
