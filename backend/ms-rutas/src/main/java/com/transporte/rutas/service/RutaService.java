package com.transporte.rutas.service;

import com.transporte.rutas.dto.HorarioDTO;
import com.transporte.rutas.dto.ParadaDTO;
import com.transporte.rutas.dto.RutaDTO;
import com.transporte.rutas.entity.Ruta;
import com.transporte.rutas.repository.RutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;

    @Transactional(readOnly = true)
    public List<RutaDTO> obtenerTodas() {
        return rutaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RutaDTO obtenerPorId(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con id: " + id));
        return convertirADTOCompleto(ruta);
    }

    @Transactional(readOnly = true)
    public RutaDTO obtenerPorCodigo(String codigo) {
        Ruta ruta = rutaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con codigo: " + codigo));
        return convertirADTOCompleto(ruta);
    }

    @Transactional(readOnly = true)
    public List<RutaDTO> obtenerActivas() {
        return rutaRepository.findByActiva(true)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RutaDTO> buscarPorNombre(String nombre) {
        return rutaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RutaDTO crear(RutaDTO dto) {
        if (rutaRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una ruta con el codigo: " + dto.getCodigo());
        }
        Ruta ruta = convertirAEntidad(dto);
        ruta = rutaRepository.save(ruta);
        return convertirADTO(ruta);
    }

    @Transactional
    public RutaDTO actualizar(Long id, RutaDTO dto) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con id: " + id));

        if (!ruta.getCodigo().equals(dto.getCodigo()) &&
            rutaRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe otra ruta con el codigo: " + dto.getCodigo());
        }

        ruta.setCodigo(dto.getCodigo());
        ruta.setNombre(dto.getNombre());
        ruta.setDescripcion(dto.getDescripcion());
        ruta.setOrigen(dto.getOrigen());
        ruta.setDestino(dto.getDestino());
        ruta.setDistanciaKm(dto.getDistanciaKm());
        ruta.setActiva(dto.getActiva());

        ruta = rutaRepository.save(ruta);
        return convertirADTO(ruta);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!rutaRepository.existsById(id)) {
            throw new EntityNotFoundException("Ruta no encontrada con id: " + id);
        }
        rutaRepository.deleteById(id);
    }

    private RutaDTO convertirADTO(Ruta ruta) {
        RutaDTO dto = new RutaDTO();
        dto.setId(ruta.getId());
        dto.setCodigo(ruta.getCodigo());
        dto.setNombre(ruta.getNombre());
        dto.setDescripcion(ruta.getDescripcion());
        dto.setOrigen(ruta.getOrigen());
        dto.setDestino(ruta.getDestino());
        dto.setDistanciaKm(ruta.getDistanciaKm());
        dto.setActiva(ruta.getActiva());
        return dto;
    }

    private RutaDTO convertirADTOCompleto(Ruta ruta) {
        RutaDTO dto = convertirADTO(ruta);

        List<ParadaDTO> paradasDTO = ruta.getParadas().stream()
                .map(p -> {
                    ParadaDTO pDTO = new ParadaDTO();
                    pDTO.setId(p.getId());
                    pDTO.setNombre(p.getNombre());
                    pDTO.setDireccion(p.getDireccion());
                    pDTO.setLatitud(p.getLatitud());
                    pDTO.setLongitud(p.getLongitud());
                    pDTO.setOrden(p.getOrden());
                    pDTO.setRutaId(ruta.getId());
                    return pDTO;
                })
                .collect(Collectors.toList());

        List<HorarioDTO> horariosDTO = ruta.getHorarios().stream()
                .map(h -> {
                    HorarioDTO hDTO = new HorarioDTO();
                    hDTO.setId(h.getId());
                    hDTO.setRutaId(ruta.getId());
                    hDTO.setDiaSemana(h.getDiaSemana());
                    hDTO.setHoraInicio(h.getHoraInicio());
                    hDTO.setHoraFin(h.getHoraFin());
                    hDTO.setFrecuenciaMinutos(h.getFrecuenciaMinutos());
                    hDTO.setActivo(h.getActivo());
                    return hDTO;
                })
                .collect(Collectors.toList());

        dto.setParadas(paradasDTO);
        dto.setHorarios(horariosDTO);
        return dto;
    }

    private Ruta convertirAEntidad(RutaDTO dto) {
        Ruta ruta = new Ruta();
        ruta.setCodigo(dto.getCodigo());
        ruta.setNombre(dto.getNombre());
        ruta.setDescripcion(dto.getDescripcion());
        ruta.setOrigen(dto.getOrigen());
        ruta.setDestino(dto.getDestino());
        ruta.setDistanciaKm(dto.getDistanciaKm());
        ruta.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        return ruta;
    }
}
