package com.transporte.rutas.controller;

import com.transporte.rutas.dto.ParadaDTO;
import com.transporte.rutas.service.ParadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paradas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ParadaController {

    private final ParadaService paradaService;

    @GetMapping
    public ResponseEntity<List<ParadaDTO>> listarTodas() {
        List<ParadaDTO> paradas = paradaService.obtenerTodas();
        return ResponseEntity.ok(paradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParadaDTO> obtenerPorId(@PathVariable Long id) {
        ParadaDTO parada = paradaService.obtenerPorId(id);
        return ResponseEntity.ok(parada);
    }

    @GetMapping("/ruta/{rutaId}")
    public ResponseEntity<List<ParadaDTO>> listarPorRuta(@PathVariable Long rutaId) {
        List<ParadaDTO> paradas = paradaService.obtenerPorRuta(rutaId);
        return ResponseEntity.ok(paradas);
    }

    @PostMapping
    public ResponseEntity<ParadaDTO> crear(@Valid @RequestBody ParadaDTO paradaDTO) {
        ParadaDTO nuevaParada = paradaService.crear(paradaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaParada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParadaDTO> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody ParadaDTO paradaDTO) {
        ParadaDTO paradaActualizada = paradaService.actualizar(id, paradaDTO);
        return ResponseEntity.ok(paradaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        paradaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
