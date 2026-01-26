package com.transporte.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservices.vehiculos.url}")
    private String vehiculosUrl;

    @Value("${microservices.rutas.url}")
    private String rutasUrl;

    @Bean
    public WebClient vehiculosWebClient() {
        return WebClient.builder()
                .baseUrl(vehiculosUrl)
                .build();
    }

    @Bean
    public WebClient rutasWebClient() {
        return WebClient.builder()
                .baseUrl(rutasUrl)
                .build();
    }
}
