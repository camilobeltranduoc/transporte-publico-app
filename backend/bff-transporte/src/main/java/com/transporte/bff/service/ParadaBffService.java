package com.transporte.bff.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ParadaBffService {

    private final WebClient webClient;

    public ParadaBffService(@Qualifier("rutasWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Object> obtenerTodas() {
        return webClient.get()
                .uri("/api/paradas")
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorId(Long id) {
        return webClient.get()
                .uri("/api/paradas/{id}", id)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorRuta(Long rutaId) {
        return webClient.get()
                .uri("/api/paradas/ruta/{rutaId}", rutaId)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> crear(Map<String, Object> parada) {
        return webClient.post()
                .uri("/api/paradas")
                .bodyValue(parada)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> actualizar(Long id, Map<String, Object> parada) {
        return webClient.put()
                .uri("/api/paradas/{id}", id)
                .bodyValue(parada)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Void> eliminar(Long id) {
        return webClient.delete()
                .uri("/api/paradas/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
