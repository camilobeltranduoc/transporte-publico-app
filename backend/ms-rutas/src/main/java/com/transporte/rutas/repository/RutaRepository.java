package com.transporte.rutas.repository;

import com.transporte.rutas.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    Optional<Ruta> findByCodigo(String codigo);

    List<Ruta> findByActiva(Boolean activa);

    List<Ruta> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByCodigo(String codigo);
}
