package com.transporte.rutas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaDTO {

    private Long id;

    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    private String origen;

    private String destino;

    private Double distanciaKm;

    private Boolean activa;

    private List<ParadaDTO> paradas;

    private List<HorarioDTO> horarios;
}
