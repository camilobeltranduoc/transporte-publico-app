package com.transporte.bff.controller;

import com.transporte.bff.service.UbicacionBffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionBffController {

    private final UbicacionBffService ubicacionService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarUbicaciones() {
        return ubicacionService.obtenerTodas()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerUbicacion(@PathVariable Long id) {
        return ubicacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public Mono<ResponseEntity<Object>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        return ubicacionService.obtenerPorVehiculo(vehiculoId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehiculo/{vehiculoId}/ultima")
    public Mono<ResponseEntity<Object>> obtenerUltimaUbicacion(@PathVariable Long vehiculoId) {
        return ubicacionService.obtenerUltimaUbicacion(vehiculoId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarUbicacion(@RequestBody Map<String, Object> ubicacion) {
        return ubicacionService.registrar(ubicacion)
                .map(u -> ResponseEntity.status(201).body(u));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarUbicacion(@PathVariable Long id) {
        return ubicacionService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
