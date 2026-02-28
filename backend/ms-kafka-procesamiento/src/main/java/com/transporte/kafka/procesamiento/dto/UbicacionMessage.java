package com.transporte.kafka.procesamiento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionMessage {
    private Long idVehiculo;
    private Double latitud;
    private Double longitud;
    private String timestamp;
    private String nombreParada;
}
