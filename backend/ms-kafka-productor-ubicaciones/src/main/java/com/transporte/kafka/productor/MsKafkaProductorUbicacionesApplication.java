package com.transporte.kafka.productor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsKafkaProductorUbicacionesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsKafkaProductorUbicacionesApplication.class, args);
    }
}
