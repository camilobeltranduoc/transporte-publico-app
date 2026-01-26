package com.transporte.bff.controller;

import com.transporte.bff.service.HorarioBffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioBffController {

    private final HorarioBffService horarioService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarHorarios() {
        return horarioService.obtenerTodos()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerHorario(@PathVariable Long id) {
        return horarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ruta/{rutaId}")
    public Mono<ResponseEntity<Object>> listarPorRuta(@PathVariable Long rutaId) {
        return horarioService.obtenerPorRuta(rutaId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/ruta/{rutaId}/dia/{dia}")
    public Mono<ResponseEntity<Object>> listarPorRutaYDia(@PathVariable Long rutaId,
                                                           @PathVariable String dia) {
        return horarioService.obtenerPorRutaYDia(rutaId, dia)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearHorario(@RequestBody Map<String, Object> horario) {
        return horarioService.crear(horario)
                .map(h -> ResponseEntity.status(201).body(h));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> actualizarHorario(@PathVariable Long id,
                                                           @RequestBody Map<String, Object> horario) {
        return horarioService.actualizar(id, horario)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarHorario(@PathVariable Long id) {
        return horarioService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
