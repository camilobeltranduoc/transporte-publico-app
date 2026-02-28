package com.transporte.kafka.consumidor.gps.repository;

import com.transporte.kafka.consumidor.gps.entity.KafkaHistorialGps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KafkaHistorialGpsRepository extends JpaRepository<KafkaHistorialGps, Long> {
    List<KafkaHistorialGps> findByIdVehiculo(Long idVehiculo);
    List<KafkaHistorialGps> findByFechaRegistroStartingWith(String fecha);
}
