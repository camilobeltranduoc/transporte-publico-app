package com.transporte.bff.controller;

import com.transporte.bff.service.RutaBffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaBffController {

    private final RutaBffService rutaService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarRutas() {
        return rutaService.obtenerTodas()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerRuta(@PathVariable Long id) {
        return rutaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public Mono<ResponseEntity<Object>> obtenerPorCodigo(@PathVariable String codigo) {
        return rutaService.obtenerPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/activas")
    public Mono<ResponseEntity<Object>> listarActivas() {
        return rutaService.obtenerActivas()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public Mono<ResponseEntity<Object>> buscarPorNombre(@RequestParam String nombre) {
        return rutaService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearRuta(@RequestBody Map<String, Object> ruta) {
        return rutaService.crear(ruta)
                .map(r -> ResponseEntity.status(201).body(r));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> actualizarRuta(@PathVariable Long id,
                                                        @RequestBody Map<String, Object> ruta) {
        return rutaService.actualizar(id, ruta)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarRuta(@PathVariable Long id) {
        return rutaService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
