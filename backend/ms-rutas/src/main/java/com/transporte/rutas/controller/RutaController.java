package com.transporte.rutas.controller;

import com.transporte.rutas.dto.RutaDTO;
import com.transporte.rutas.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RutaController {

    private final RutaService rutaService;

    @GetMapping
    public ResponseEntity<List<RutaDTO>> listarTodas() {
        List<RutaDTO> rutas = rutaService.obtenerTodas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutaDTO> obtenerPorId(@PathVariable Long id) {
        RutaDTO ruta = rutaService.obtenerPorId(id);
        return ResponseEntity.ok(ruta);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<RutaDTO> obtenerPorCodigo(@PathVariable String codigo) {
        RutaDTO ruta = rutaService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(ruta);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<RutaDTO>> listarActivas() {
        List<RutaDTO> rutas = rutaService.obtenerActivas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RutaDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<RutaDTO> rutas = rutaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(rutas);
    }

    @PostMapping
    public ResponseEntity<RutaDTO> crear(@Valid @RequestBody RutaDTO rutaDTO) {
        RutaDTO nuevaRuta = rutaService.crear(rutaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRuta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutaDTO> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody RutaDTO rutaDTO) {
        RutaDTO rutaActualizada = rutaService.actualizar(id, rutaDTO);
        return ResponseEntity.ok(rutaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rutaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
