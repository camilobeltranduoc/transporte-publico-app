package com.transporte.consumidor.gps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpsMessage {
    private Long idVehiculo;
    private Double latitud;
    private Double longitud;
    private String timestamp;
}
