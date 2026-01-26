package com.transporte.vehiculos.dto;

import com.transporte.vehiculos.entity.Vehiculo.EstadoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {

    private Long id;

    @NotBlank(message = "La patente es obligatoria")
    private String patente;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    private String marca;

    private String modelo;

    private Integer capacidad;

    @NotNull(message = "El estado es obligatorio")
    private EstadoVehiculo estado;
}
