package com.transporte.bff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HorarioBffService {

    @Qualifier("rutasWebClient")
    private final WebClient webClient;

    public Mono<Object> obtenerTodos() {
        return webClient.get()
                .uri("/api/horarios")
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorId(Long id) {
        return webClient.get()
                .uri("/api/horarios/{id}", id)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorRuta(Long rutaId) {
        return webClient.get()
                .uri("/api/horarios/ruta/{rutaId}", rutaId)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorRutaYDia(Long rutaId, String dia) {
        return webClient.get()
                .uri("/api/horarios/ruta/{rutaId}/dia/{dia}", rutaId, dia)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> crear(Map<String, Object> horario) {
        return webClient.post()
                .uri("/api/horarios")
                .bodyValue(horario)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> actualizar(Long id, Map<String, Object> horario) {
        return webClient.put()
                .uri("/api/horarios/{id}", id)
                .bodyValue(horario)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Void> eliminar(Long id) {
        return webClient.delete()
                .uri("/api/horarios/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
