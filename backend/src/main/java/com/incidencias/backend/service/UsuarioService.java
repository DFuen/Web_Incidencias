package com.incidencias.backend.service;

import com.incidencias.backend.model.Usuario;
import com.incidencias.backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRolIgnoreCase(rol);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {

        usuario.setRol(
                usuario.getRol().toLowerCase()
        );

        usuario.setPassword(
                passwordEncoder.encode(usuario.getPassword())
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario datos) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(datos.getNombre());

        usuario.setEmail(datos.getEmail());

        usuario.setRol(
                datos.getRol().toLowerCase()
        );

        if (
                datos.getPassword() != null &&
                !datos.getPassword().isBlank()
        ) {

            usuario.setPassword(
                    passwordEncoder.encode(datos.getPassword())
            );
        }

        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}