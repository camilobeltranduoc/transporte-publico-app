package com.transporte.rutas.controller;

import com.transporte.rutas.dto.HorarioDTO;
import com.transporte.rutas.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HorarioController {

    private final HorarioService horarioService;

    @GetMapping
    public ResponseEntity<List<HorarioDTO>> listarTodos() {
        List<HorarioDTO> horarios = horarioService.obtenerTodos();
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioDTO> obtenerPorId(@PathVariable Long id) {
        HorarioDTO horario = horarioService.obtenerPorId(id);
        return ResponseEntity.ok(horario);
    }

    @GetMapping("/ruta/{rutaId}")
    public ResponseEntity<List<HorarioDTO>> listarPorRuta(@PathVariable Long rutaId) {
        List<HorarioDTO> horarios = horarioService.obtenerPorRuta(rutaId);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/ruta/{rutaId}/dia/{dia}")
    public ResponseEntity<List<HorarioDTO>> listarPorRutaYDia(@PathVariable Long rutaId,
                                                              @PathVariable DayOfWeek dia) {
        List<HorarioDTO> horarios = horarioService.obtenerPorRutaYDia(rutaId, dia);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/activos/dia/{dia}")
    public ResponseEntity<List<HorarioDTO>> listarActivosPorDia(@PathVariable DayOfWeek dia) {
        List<HorarioDTO> horarios = horarioService.obtenerActivosPorDia(dia);
        return ResponseEntity.ok(horarios);
    }

    @PostMapping
    public ResponseEntity<HorarioDTO> crear(@Valid @RequestBody HorarioDTO horarioDTO) {
        HorarioDTO nuevoHorario = horarioService.crear(horarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHorario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioDTO> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody HorarioDTO horarioDTO) {
        HorarioDTO horarioActualizado = horarioService.actualizar(id, horarioDTO);
        return ResponseEntity.ok(horarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
