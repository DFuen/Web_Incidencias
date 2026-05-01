package com.incidencias.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambioEstadoRequest {
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    private String descripcionSolucion;

    private Long encargadoId;
}
