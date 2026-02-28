package com.transporte.kafka.consumidor.horarios.repository;

import com.transporte.kafka.consumidor.horarios.entity.KafkaHistorialHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KafkaHistorialHorarioRepository extends JpaRepository<KafkaHistorialHorario, Long> {
    List<KafkaHistorialHorario> findByIdVehiculo(Long idVehiculo);
    List<KafkaHistorialHorario> findByNombreParada(String nombreParada);
    List<KafkaHistorialHorario> findByFechaRegistroStartingWith(String fecha);
}
