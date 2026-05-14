
package com.incidencias.backend.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.incidencias.backend.model.Incidencia;
import com.incidencias.backend.model.Usuario;
import com.incidencias.backend.repository.IncidenciaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class IncidenciaService {
    private final IncidenciaRepository incidenciaRepository;

    public IncidenciaService(IncidenciaRepository incidenciaRepository) {
        this.incidenciaRepository = incidenciaRepository;
    }

    public Page<Incidencia> findAll(Specification<Incidencia> spec, Pageable pageable) {
        return incidenciaRepository.findAll(spec, pageable);
    }

    public List<Incidencia> findAll(Specification<Incidencia> spec) {
        return incidenciaRepository.findAll(spec);
    }

    public List<Incidencia> findByUsuario(Usuario usuario) {
        return incidenciaRepository.findByUsuarioCreador(usuario);
    }

    public Optional<Incidencia> findById(Long id) {
        return incidenciaRepository.findById(id);
    }

    public Incidencia save(Incidencia incidencia) {
        if (incidencia.getFechaCreacion() == null) {
            incidencia.setFechaCreacion(LocalDateTime.now());
        }
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia cambiarEstado(Long id, Incidencia.EstadoIncidencia nuevoEstado, String descripcionSolucion, Usuario usuarioActual) {
        Optional<Incidencia> incidenciaOpt = incidenciaRepository.findById(id);
        if (incidenciaOpt.isPresent()) {
            Incidencia incidencia = incidenciaOpt.get();
            if (incidencia.getEstado() == Incidencia.EstadoIncidencia.REALIZADA) {
                throw new IllegalStateException("No se puede modificar una incidencia ya realizada");
            }
            incidencia.setEstado(nuevoEstado);

            if (nuevoEstado == Incidencia.EstadoIncidencia.EN_PROCESO) {
                incidencia.setUsuarioEncargado(usuarioActual);
            }

            if (nuevoEstado == Incidencia.EstadoIncidencia.REALIZADA) {
                incidencia.setFechaResolucion(LocalDateTime.now());
                String solucionLimpia = descripcionSolucion != null ? descripcionSolucion.trim() : null;
                incidencia.setDescripcionSolucion(solucionLimpia == null || solucionLimpia.isEmpty() ? null : solucionLimpia);
            }
            return incidenciaRepository.save(incidencia);
        }
        return null;
    }
}
