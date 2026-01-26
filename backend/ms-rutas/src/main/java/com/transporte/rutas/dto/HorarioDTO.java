package com.transporte.rutas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {

    private Long id;

    private Long rutaId;

    private String codigoRuta;

    @NotNull(message = "El dia de la semana es obligatorio")
    private DayOfWeek diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Integer frecuenciaMinutos;

    private Boolean activo;
}
