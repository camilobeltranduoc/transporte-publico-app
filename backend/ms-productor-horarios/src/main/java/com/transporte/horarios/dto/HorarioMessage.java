package com.transporte.horarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioMessage {
    private String idRuta;
    private String idParada;
    private String horaLlegada;
    private String estado;
}
