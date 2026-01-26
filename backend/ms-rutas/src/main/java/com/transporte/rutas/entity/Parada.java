package com.transporte.rutas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paradas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la parada es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String direccion;

    @NotNull(message = "La latitud es obligatoria")
    @Column(nullable = false)
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    @Column(nullable = false)
    private Double longitud;

    @Column(name = "orden_parada")
    private Integer orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;
}
