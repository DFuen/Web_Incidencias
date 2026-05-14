package com.incidencias.backend.service;

import com.incidencias.backend.model.Ubicacion;
import com.incidencias.backend.repository.UbicacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UbicacionService {
    private final UbicacionRepository ubicacionRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    public List<Ubicacion> findAll() {
        return ubicacionRepository.findAll();
    }

    public Ubicacion save(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }
}
