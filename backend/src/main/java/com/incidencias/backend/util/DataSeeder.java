package com.incidencias.backend.util;

import com.incidencias.backend.model.Usuario;
import com.incidencias.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Usuario admin = usuarioRepository
                .findByEmail("admin@centro.es")
                .orElse(new Usuario());

        admin.setNombre("Administrador Principal");
        admin.setEmail("admin@centro.es");
        admin.setRol("administrador");
        admin.setPassword(passwordEncoder.encode("123456"));

        usuarioRepository.save(admin);

        Usuario profesor = usuarioRepository
                .findByEmail("profesor@centro.es")
                .orElse(new Usuario());

        profesor.setNombre("Profesor Demo");
        profesor.setEmail("profesor@centro.es");
        profesor.setRol("profesor");
        profesor.setPassword(passwordEncoder.encode("123456"));

        usuarioRepository.save(profesor);
    }
}
