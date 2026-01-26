package com.transporte.bff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VehiculoBffService {

    @Qualifier("vehiculosWebClient")
    private final WebClient webClient;

    public Mono<Object> obtenerTodos() {
        return webClient.get()
                .uri("/api/vehiculos")
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorId(Long id) {
        return webClient.get()
                .uri("/api/vehiculos/{id}", id)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorPatente(String patente) {
        return webClient.get()
                .uri("/api/vehiculos/patente/{patente}", patente)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorEstado(String estado) {
        return webClient.get()
                .uri("/api/vehiculos/estado/{estado}", estado)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> crear(Map<String, Object> vehiculo) {
        return webClient.post()
                .uri("/api/vehiculos")
                .bodyValue(vehiculo)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> actualizar(Long id, Map<String, Object> vehiculo) {
        return webClient.put()
                .uri("/api/vehiculos/{id}", id)
                .bodyValue(vehiculo)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Void> eliminar(Long id) {
        return webClient.delete()
                .uri("/api/vehiculos/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
