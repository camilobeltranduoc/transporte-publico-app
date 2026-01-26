package com.transporte.rutas.repository;

import com.transporte.rutas.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByRutaId(Long rutaId);

    List<Horario> findByRutaIdAndDiaSemana(Long rutaId, DayOfWeek diaSemana);

    List<Horario> findByDiaSemanaAndActivoTrue(DayOfWeek diaSemana);
}
