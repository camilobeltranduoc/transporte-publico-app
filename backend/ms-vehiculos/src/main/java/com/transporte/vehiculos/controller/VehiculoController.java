package com.transporte.vehiculos.controller;

import com.transporte.vehiculos.dto.VehiculoDTO;
import com.transporte.vehiculos.entity.Vehiculo.EstadoVehiculo;
import com.transporte.vehiculos.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listarTodos() {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerTodos();
        return ResponseEntity.ok(vehiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
        VehiculoDTO vehiculo = vehiculoService.obtenerPorId(id);
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping("/patente/{patente}")
    public ResponseEntity<VehiculoDTO> obtenerPorPatente(@PathVariable String patente) {
        VehiculoDTO vehiculo = vehiculoService.obtenerPorPatente(patente);
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> listarPorEstado(@PathVariable EstadoVehiculo estado) {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(vehiculos);
    }

    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(@Valid @RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO nuevoVehiculo = vehiculoService.crear(vehiculoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVehiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO vehiculoActualizado = vehiculoService.actualizar(id, vehiculoDTO);
        return ResponseEntity.ok(vehiculoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
