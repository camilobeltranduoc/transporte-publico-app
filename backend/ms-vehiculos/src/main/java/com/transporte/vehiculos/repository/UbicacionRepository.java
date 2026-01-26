package com.transporte.vehiculos.repository;

import com.transporte.vehiculos.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    List<Ubicacion> findByVehiculoId(Long vehiculoId);

    @Query("SELECT u FROM Ubicacion u WHERE u.vehiculo.id = :vehiculoId ORDER BY u.fechaRegistro DESC")
    List<Ubicacion> findUltimasUbicaciones(@Param("vehiculoId") Long vehiculoId);

    @Query("SELECT u FROM Ubicacion u WHERE u.vehiculo.id = :vehiculoId ORDER BY u.fechaRegistro DESC LIMIT 1")
    Optional<Ubicacion> findUltimaUbicacion(@Param("vehiculoId") Long vehiculoId);

    List<Ubicacion> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);
}
