package com.transporte.vehiculos.controller;

import com.transporte.vehiculos.dto.UbicacionDTO;
import com.transporte.vehiculos.service.UbicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> listarTodas() {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerTodas();
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> obtenerPorId(@PathVariable Long id) {
        UbicacionDTO ubicacion = ubicacionService.obtenerPorId(id);
        return ResponseEntity.ok(ubicacion);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<UbicacionDTO>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerPorVehiculo(vehiculoId);
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/vehiculo/{vehiculoId}/ultima")
    public ResponseEntity<UbicacionDTO> obtenerUltimaUbicacion(@PathVariable Long vehiculoId) {
        UbicacionDTO ubicacion = ubicacionService.obtenerUltimaUbicacion(vehiculoId);
        return ResponseEntity.ok(ubicacion);
    }

    @PostMapping
    public ResponseEntity<UbicacionDTO> registrar(@Valid @RequestBody UbicacionDTO ubicacionDTO) {
        UbicacionDTO nuevaUbicacion = ubicacionService.registrar(ubicacionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUbicacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
