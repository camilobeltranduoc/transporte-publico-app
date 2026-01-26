package com.transporte.bff.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class UbicacionBffService {

    private final WebClient webClient;

    public UbicacionBffService(@Qualifier("vehiculosWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Object> obtenerTodas() {
        return webClient.get()
                .uri("/api/ubicaciones")
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorId(Long id) {
        return webClient.get()
                .uri("/api/ubicaciones/{id}", id)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerPorVehiculo(Long vehiculoId) {
        return webClient.get()
                .uri("/api/ubicaciones/vehiculo/{vehiculoId}", vehiculoId)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> obtenerUltimaUbicacion(Long vehiculoId) {
        return webClient.get()
                .uri("/api/ubicaciones/vehiculo/{vehiculoId}/ultima", vehiculoId)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> registrar(Map<String, Object> ubicacion) {
        return webClient.post()
                .uri("/api/ubicaciones")
                .bodyValue(ubicacion)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Void> eliminar(Long id) {
        return webClient.delete()
                .uri("/api/ubicaciones/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
