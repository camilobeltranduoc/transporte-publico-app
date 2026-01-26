package com.transporte.rutas.repository;

import com.transporte.rutas.entity.Parada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, Long> {

    List<Parada> findByRutaIdOrderByOrden(Long rutaId);

    List<Parada> findByNombreContainingIgnoreCase(String nombre);
}
