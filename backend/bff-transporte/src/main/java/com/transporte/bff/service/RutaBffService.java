package com.transporte.bff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RutaBffService {

    @Qualifier("rutasWebClient")
    private final WebClient webClient;

    public Mono<Object> obtenerTodas() {
        return webClient.get()
                .uri("/api/rutas")
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorId(Long id) {
        return webClient.get()
                .uri("/api/rutas/{id}", id)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorCodigo(String codigo) {
        return webClient.get()
                .uri("/api/rutas/codigo/{codigo}", codigo)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerActivas() {
        return webClient.get()
                .uri("/api/rutas/activas")
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> buscarPorNombre(String nombre) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/rutas/buscar")
                        .queryParam("nombre", nombre)
                        .build())
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> crear(Map<String, Object> ruta) {
        return webClient.post()
                .uri("/api/rutas")
                .bodyValue(ruta)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> actualizar(Long id, Map<String, Object> ruta) {
        return webClient.put()
                .uri("/api/rutas/{id}", id)
                .bodyValue(ruta)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Void> eliminar(Long id) {
        return webClient.delete()
                .uri("/api/rutas/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
