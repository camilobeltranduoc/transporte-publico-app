package com.transporte.bff.controller;

import com.transporte.bff.service.VehiculoBffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoBffController {

    private final VehiculoBffService vehiculoService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarVehiculos() {
        return vehiculoService.obtenerTodos()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerVehiculo(@PathVariable Long id) {
        return vehiculoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/patente/{patente}")
    public Mono<ResponseEntity<Object>> obtenerPorPatente(@PathVariable String patente) {
        return vehiculoService.obtenerPorPatente(patente)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public Mono<ResponseEntity<Object>> listarPorEstado(@PathVariable String estado) {
        return vehiculoService.obtenerPorEstado(estado)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearVehiculo(@RequestBody Map<String, Object> vehiculo) {
        return vehiculoService.crear(vehiculo)
                .map(v -> ResponseEntity.status(201).body(v));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> actualizarVehiculo(@PathVariable Long id,
                                                           @RequestBody Map<String, Object> vehiculo) {
        return vehiculoService.actualizar(id, vehiculo)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarVehiculo(@PathVariable Long id) {
        return vehiculoService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
