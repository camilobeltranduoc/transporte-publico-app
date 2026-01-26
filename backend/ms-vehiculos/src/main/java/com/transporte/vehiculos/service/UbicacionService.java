package com.transporte.vehiculos.service;

import com.transporte.vehiculos.dto.UbicacionDTO;
import com.transporte.vehiculos.entity.Ubicacion;
import com.transporte.vehiculos.entity.Vehiculo;
import com.transporte.vehiculos.repository.UbicacionRepository;
import com.transporte.vehiculos.repository.VehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final VehiculoRepository vehiculoRepository;

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerTodas() {
        return ubicacionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UbicacionDTO obtenerPorId(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ubicacion no encontrada con id: " + id));
        return convertirADTO(ubicacion);
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerPorVehiculo(Long vehiculoId) {
        return ubicacionRepository.findByVehiculoId(vehiculoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UbicacionDTO obtenerUltimaUbicacion(Long vehiculoId) {
        Ubicacion ubicacion = ubicacionRepository.findUltimaUbicacion(vehiculoId)
                .orElseThrow(() -> new EntityNotFoundException("No hay ubicaciones para el vehiculo: " + vehiculoId));
        return convertirADTO(ubicacion);
    }

    @Transactional
    public UbicacionDTO registrar(UbicacionDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado con id: " + dto.getVehiculoId()));

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setVehiculo(vehiculo);
        ubicacion.setLatitud(dto.getLatitud());
        ubicacion.setLongitud(dto.getLongitud());
        ubicacion.setVelocidad(dto.getVelocidad());

        ubicacion = ubicacionRepository.save(ubicacion);
        return convertirADTO(ubicacion);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new EntityNotFoundException("Ubicacion no encontrada con id: " + id);
        }
        ubicacionRepository.deleteById(id);
    }

    private UbicacionDTO convertirADTO(Ubicacion ubicacion) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(ubicacion.getId());
        dto.setVehiculoId(ubicacion.getVehiculo().getId());
        dto.setPatenteVehiculo(ubicacion.getVehiculo().getPatente());
        dto.setLatitud(ubicacion.getLatitud());
        dto.setLongitud(ubicacion.getLongitud());
        dto.setVelocidad(ubicacion.getVelocidad());
        dto.setFechaRegistro(ubicacion.getFechaRegistro());
        return dto;
    }
}
