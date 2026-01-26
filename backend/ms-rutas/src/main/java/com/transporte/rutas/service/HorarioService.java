package com.transporte.rutas.service;

import com.transporte.rutas.dto.HorarioDTO;
import com.transporte.rutas.entity.Horario;
import com.transporte.rutas.entity.Ruta;
import com.transporte.rutas.repository.HorarioRepository;
import com.transporte.rutas.repository.RutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final RutaRepository rutaRepository;

    @Transactional(readOnly = true)
    public List<HorarioDTO> obtenerTodos() {
        return horarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HorarioDTO obtenerPorId(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado con id: " + id));
        return convertirADTO(horario);
    }

    @Transactional(readOnly = true)
    public List<HorarioDTO> obtenerPorRuta(Long rutaId) {
        return horarioRepository.findByRutaId(rutaId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HorarioDTO> obtenerPorRutaYDia(Long rutaId, DayOfWeek dia) {
        return horarioRepository.findByRutaIdAndDiaSemana(rutaId, dia)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HorarioDTO> obtenerActivosPorDia(DayOfWeek dia) {
        return horarioRepository.findByDiaSemanaAndActivoTrue(dia)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public HorarioDTO crear(HorarioDTO dto) {
        Ruta ruta = rutaRepository.findById(dto.getRutaId())
                .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con id: " + dto.getRutaId()));

        Horario horario = new Horario();
        horario.setRuta(ruta);
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setFrecuenciaMinutos(dto.getFrecuenciaMinutos());
        horario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        horario = horarioRepository.save(horario);
        return convertirADTO(horario);
    }

    @Transactional
    public HorarioDTO actualizar(Long id, HorarioDTO dto) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado con id: " + id));

        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setFrecuenciaMinutos(dto.getFrecuenciaMinutos());
        horario.setActivo(dto.getActivo());

        if (dto.getRutaId() != null && !dto.getRutaId().equals(horario.getRuta().getId())) {
            Ruta nuevaRuta = rutaRepository.findById(dto.getRutaId())
                    .orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada con id: " + dto.getRutaId()));
            horario.setRuta(nuevaRuta);
        }

        horario = horarioRepository.save(horario);
        return convertirADTO(horario);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Horario no encontrado con id: " + id);
        }
        horarioRepository.deleteById(id);
    }

    private HorarioDTO convertirADTO(Horario horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setId(horario.getId());
        dto.setRutaId(horario.getRuta().getId());
        dto.setCodigoRuta(horario.getRuta().getCodigo());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setFrecuenciaMinutos(horario.getFrecuenciaMinutos());
        dto.setActivo(horario.getActivo());
        return dto;
    }
}
