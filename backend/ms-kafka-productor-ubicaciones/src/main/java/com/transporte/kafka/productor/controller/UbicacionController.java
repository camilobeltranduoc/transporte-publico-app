package com.transporte.kafka.productor.controller;

import com.transporte.kafka.productor.dto.UbicacionMessage;
import com.transporte.kafka.productor.service.UbicacionProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UbicacionController {

    private final UbicacionProducerService producerService;

    @PostMapping
    public ResponseEntity<String> publicarUbicacion(@RequestBody UbicacionMessage mensaje) {
        producerService.publicarUbicacion(mensaje);
        return ResponseEntity.accepted().body("Ubicacion publicada en Kafka: " + mensaje.getIdVehiculo());
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ms-kafka-productor-ubicaciones UP");
    }
}
