package com.transporte.kafka.consumidor.horarios.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "kafka_historial_horarios")
public class KafkaHistorialHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idVehiculo;
    private String nombreParada;
    private String horarioLlegada;
    private String timestamp;
    private String estado;
    private String fechaRegistro;
}
