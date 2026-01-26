package com.transporte.vehiculos.service;

import com.transporte.vehiculos.dto.VehiculoDTO;
import com.transporte.vehiculos.entity.Vehiculo;
import com.transporte.vehiculos.entity.Vehiculo.EstadoVehiculo;
import com.transporte.vehiculos.repository.VehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Transactional(readOnly = true)
    public List<VehiculoDTO> obtenerTodos() {
        return vehiculoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehiculoDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado con id: " + id));
        return convertirADTO(vehiculo);
    }

    @Transactional(readOnly = true)
    public VehiculoDTO obtenerPorPatente(String patente) {
        Vehiculo vehiculo = vehiculoRepository.findByPatente(patente)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado con patente: " + patente));
        return convertirADTO(vehiculo);
    }

    @Transactional(readOnly = true)
    public List<VehiculoDTO> obtenerPorEstado(EstadoVehiculo estado) {
        return vehiculoRepository.findByEstado(estado)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehiculoDTO crear(VehiculoDTO dto) {
        if (vehiculoRepository.existsByPatente(dto.getPatente())) {
            throw new IllegalArgumentException("Ya existe un vehiculo con la patente: " + dto.getPatente());
        }
        Vehiculo vehiculo = convertirAEntidad(dto);
        vehiculo = vehiculoRepository.save(vehiculo);
        return convertirADTO(vehiculo);
    }

    @Transactional
    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado con id: " + id));

        if (!vehiculo.getPatente().equals(dto.getPatente()) &&
            vehiculoRepository.existsByPatente(dto.getPatente())) {
            throw new IllegalArgumentException("Ya existe otro vehiculo con la patente: " + dto.getPatente());
        }

        vehiculo.setPatente(dto.getPatente());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setCapacidad(dto.getCapacidad());
        vehiculo.setEstado(dto.getEstado());

        vehiculo = vehiculoRepository.save(vehiculo);
        return convertirADTO(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new EntityNotFoundException("Vehiculo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    private VehiculoDTO convertirADTO(Vehiculo vehiculo) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(vehiculo.getId());
        dto.setPatente(vehiculo.getPatente());
        dto.setTipo(vehiculo.getTipo());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());
        dto.setCapacidad(vehiculo.getCapacidad());
        dto.setEstado(vehiculo.getEstado());
        return dto;
    }

    private Vehiculo convertirAEntidad(VehiculoDTO dto) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente(dto.getPatente());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setCapacidad(dto.getCapacidad());
        vehiculo.setEstado(dto.getEstado());
        return vehiculo;
    }
}
