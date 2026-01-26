package com.transporte.vehiculos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {

    private Long id;

    @NotNull(message = "El id del vehiculo es obligatorio")
    private Long vehiculoId;

    private String patenteVehiculo;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    private Double velocidad;

    private LocalDateTime fechaRegistro;
}
