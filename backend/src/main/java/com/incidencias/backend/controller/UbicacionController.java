package com.incidencias.backend.controller;

import com.incidencias.backend.model.Ubicacion;
import com.incidencias.backend.service.UbicacionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/ubicaciones")
public class UbicacionController {
    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public List<Ubicacion> getAll() {
        return ubicacionService.findAll();
    }

    @PostMapping
    public Ubicacion create(@Valid @RequestBody Ubicacion ubicacion) {
        return ubicacionService.save(ubicacion);
    }

    @PutMapping("/{id}")
    public Ubicacion update(
            @PathVariable Long id,
            @Valid @RequestBody Ubicacion ubicacion
    ) {

        Ubicacion existente =
                ubicacionService.findById(id);

        existente.setCodigo(
                ubicacion.getCodigo()
        );

        existente.setNombre(
                ubicacion.getNombre()
        );

        return ubicacionService.save(existente);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        ubicacionService.delete(id);
    }

}
