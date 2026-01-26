package com.transporte.bff.controller;

import com.transporte.bff.service.ParadaBffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/paradas")
@RequiredArgsConstructor
public class ParadaBffController {

    private final ParadaBffService paradaService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarParadas() {
        return paradaService.obtenerTodas()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerParada(@PathVariable Long id) {
        return paradaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ruta/{rutaId}")
    public Mono<ResponseEntity<Object>> listarPorRuta(@PathVariable Long rutaId) {
        return paradaService.obtenerPorRuta(rutaId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearParada(@RequestBody Map<String, Object> parada) {
        return paradaService.crear(parada)
                .map(p -> ResponseEntity.status(201).body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> actualizarParada(@PathVariable Long id,
                                                          @RequestBody Map<String, Object> parada) {
        return paradaService.actualizar(id, parada)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarParada(@PathVariable Long id) {
        return paradaService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
