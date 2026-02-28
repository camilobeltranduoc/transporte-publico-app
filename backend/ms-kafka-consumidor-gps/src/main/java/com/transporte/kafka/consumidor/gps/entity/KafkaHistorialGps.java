package com.transporte.kafka.consumidor.gps.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "kafka_historial_gps")
public class KafkaHistorialGps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idVehiculo;
    private Double latitud;
    private Double longitud;
    private String timestamp;
    private String nombreParada;
    private String fechaRegistro;
}
