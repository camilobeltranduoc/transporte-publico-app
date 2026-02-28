package com.transporte.kafka.consumidor.horarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioMessage {
    private Long idVehiculo;
    private String nombreParada;
    private String horarioLlegada;
    private String timestamp;
    private String estado;
}
