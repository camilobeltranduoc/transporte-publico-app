package com.transporte.kafka.procesamiento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/procesamiento")
public class ProcesamientoController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ms-kafka-procesamiento UP - escuchando ubicaciones_vehiculos -> publicando horarios");
    }
}
