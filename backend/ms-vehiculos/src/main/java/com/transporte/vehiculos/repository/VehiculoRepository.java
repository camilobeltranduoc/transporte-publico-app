package com.transporte.vehiculos.repository;

import com.transporte.vehiculos.entity.Vehiculo;
import com.transporte.vehiculos.entity.Vehiculo.EstadoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPatente(String patente);

    List<Vehiculo> findByEstado(EstadoVehiculo estado);

    List<Vehiculo> findByTipo(String tipo);

    boolean existsByPatente(String patente);
}
