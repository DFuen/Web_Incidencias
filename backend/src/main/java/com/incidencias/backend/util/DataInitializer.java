package com.incidencias.backend.util;

import com.incidencias.backend.model.*;
import com.incidencias.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(UbicacionRepository ubicacionRepo, CategoriaRepository categoriaRepo, UsuarioRepository usuarioRepo, IncidenciaRepository incidenciaRepo, PasswordEncoder encoder) {
        return args -> {
            if (ubicacionRepo.count() == 0) {
                ubicacionRepo.save(new Ubicacion(null, "1.12", "Aula 1.12"));
                ubicacionRepo.save(new Ubicacion(null, "0.3", "Laboratorio TIC"));
                ubicacionRepo.save(new Ubicacion(null, "2.5", "Biblioteca"));
            }
            if (categoriaRepo.count() == 0) {
                categoriaRepo.save(new Categoria(null, "Mantenimiento general"));
                categoriaRepo.save(new Categoria(null, "TIC"));
            }
            if (usuarioRepo.count() == 0) {
                usuarioRepo.save(new Usuario(null, "Ana Profesor", "ana@centro.edu", "profesor", encoder.encode("profesor1")));
                usuarioRepo.save(new Usuario(null, "Luis Profesor", "luis@centro.edu", "profesor", encoder.encode("profesor2")));
                usuarioRepo.save(new Usuario(null, "Marta Admin", "marta@centro.edu", "administrador", encoder.encode("admin1")));
            }
            if (incidenciaRepo.count() == 0) {
                // Aquí puedes añadir incidencias de ejemplo si lo deseas
            }
        };
    }
}
