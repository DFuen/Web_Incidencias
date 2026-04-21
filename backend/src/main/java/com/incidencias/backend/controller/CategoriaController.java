package com.incidencias.backend.controller;

import com.incidencias.backend.model.Categoria;
import com.incidencias.backend.service.CategoriaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> getAll() {
        return categoriaService.findAll();
    }

    @PostMapping
    public Categoria create(@Valid @RequestBody Categoria categoria) {
        return categoriaService.save(categoria);
    }
}
