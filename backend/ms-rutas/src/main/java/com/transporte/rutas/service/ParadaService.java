package com.transporte.rutas.service;

import com.transporte.rutas.dto.ParadaDTO;
import com.transporte.rutas.entity.Parada;
import com.transporte.rutas.entity.Ruta;
import com.transporte.rutas.repository.ParadaRepository;
import com.transporte.rutas.repository.RutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParadaService {

    private final ParadaRepository paradaRepository;
    private final RutaRepository rutaRepository;

    @Transactional(readOnly = true)
    public List<ParadaDTO> obtenerTodas() {
        return paradaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParadaDTO obtenerPorId(Long id) {
        Parada parada = paradaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parada no encontrada con id: " + id));
        return convertirADTO(parada);
    }

    @Transactional(readOnly = true)
    public List<ParadaDTO> obtenerPorRuta(Long rutaId) {
        return paradaRepository.findByRutaIdOrderByOrden(rutaId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParadaDTO crear(ParadaDTO dto) {
        Ruta ruta = rutaRepository.findById(dto.getRutaId())
                .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con id: " + dto.getRutaId()));

        Parada parada = new Parada();
        parada.setNombre(dto.getNombre());
        parada.setDireccion(dto.getDireccion());
        parada.setLatitud(dto.getLatitud());
        parada.setLongitud(dto.getLongitud());
        parada.setOrden(dto.getOrden());
        parada.setRuta(ruta);

        parada = paradaRepository.save(parada);
        return convertirADTO(parada);
    }

    @Transactional
    public ParadaDTO actualizar(Long id, ParadaDTO dto) {
        Parada parada = paradaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parada no encontrada con id: " + id));

        parada.setNombre(dto.getNombre());
        parada.setDireccion(dto.getDireccion());
        parada.setLatitud(dto.getLatitud());
        parada.setLongitud(dto.getLongitud());
        parada.setOrden(dto.getOrden());

        if (dto.getRutaId() != null && !dto.getRutaId().equals(parada.getRuta().getId())) {
            Ruta nuevaRuta = rutaRepository.findById(dto.getRutaId())
                    .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con id: " + dto.getRutaId()));
            parada.setRuta(nuevaRuta);
        }

        parada = paradaRepository.save(parada);
        return convertirADTO(parada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!paradaRepository.existsById(id)) {
            throw new EntityNotFoundException("Parada no encontrada con id: " + id);
        }
        paradaRepository.deleteById(id);
    }

    private ParadaDTO convertirADTO(Parada parada) {
        ParadaDTO dto = new ParadaDTO();
        dto.setId(parada.getId());
        dto.setNombre(parada.getNombre());
        dto.setDireccion(parada.getDireccion());
        dto.setLatitud(parada.getLatitud());
        dto.setLongitud(parada.getLongitud());
        dto.setOrden(parada.getOrden());
        dto.setRutaId(parada.getRuta().getId());
        dto.setCodigoRuta(parada.getRuta().getCodigo());
        return dto;
    }
}
