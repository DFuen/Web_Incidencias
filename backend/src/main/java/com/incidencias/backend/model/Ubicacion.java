
package com.incidencias.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Column(nullable = false, length = 10)
    private String codigo;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 100)
    private String descripcion;

}
