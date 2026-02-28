package com.transporte.kafka.consumidor.horarios.controller;

import com.transporte.kafka.consumidor.horarios.entity.KafkaHistorialHorario;
import com.transporte.kafka.consumidor.horarios.repository.KafkaHistorialHorarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-horarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HorarioHistorialController {

    private final KafkaHistorialHorarioRepository repository;

    @GetMapping
    public ResponseEntity<List<KafkaHistorialHorario>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/vehiculo/{id}")
    public ResponseEntity<List<KafkaHistorialHorario>> getByVehiculo(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findByIdVehiculo(id));
    }

    @GetMapping("/parada/{nombre}")
    public ResponseEntity<List<KafkaHistorialHorario>> getByParada(@PathVariable String nombre) {
        return ResponseEntity.ok(repository.findByNombreParada(nombre));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ms-kafka-consumidor-horarios UP - escuchando horarios");
    }
}
