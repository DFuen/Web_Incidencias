package com.incidencias.backend.repository;

import com.incidencias.backend.model.Incidencia;
import com.incidencias.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long>, JpaSpecificationExecutor<Incidencia> {
    List<Incidencia> findByUsuarioCreador(Usuario usuario);
}
