package com.incidencias.backend.controller;

import com.incidencias.backend.model.Usuario;
import com.incidencias.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/email/{email}")
    public Optional<Usuario> getByEmail(@PathVariable String email) {
        return usuarioService.findByEmail(email);
    }

    @GetMapping
    public List<Usuario> getAll(@RequestParam(required = false) String rol) {
        if (rol != null && !rol.isBlank()) {
            return usuarioService.findByRol(rol);
        }

        return usuarioService.findAll();
    }

    @PostMapping
    public Usuario create(@Valid @RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario update(
            @PathVariable Long id,
            @RequestBody Usuario usuario
    ) {
        return usuarioService.update(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }

}
