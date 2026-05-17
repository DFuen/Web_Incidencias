package com.incidencias.backend.controller;

import com.incidencias.backend.controller.dto.CambioEstadoRequest;
import com.incidencias.backend.model.Incidencia;
import com.incidencias.backend.service.IncidenciaService;
import com.incidencias.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import com.incidencias.backend.model.Incidencia.EstadoIncidencia;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Validated
@RequestMapping("/api/incidencias")
public class IncidenciaController {
    private final IncidenciaService incidenciaService;
    private final UsuarioService usuarioService;

    public IncidenciaController(IncidenciaService incidenciaService, UsuarioService usuarioService) {
        this.incidenciaService = incidenciaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public Page<Incidencia> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) EstadoIncidencia estado,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long ubicacionId,
            @RequestParam(required = false) String fechaCreacionDesde,
            @RequestParam(required = false) String fechaCreacionHasta,
            @RequestParam(required = false) String fechaResolucionDesde,
            @RequestParam(required = false) String fechaResolucionHasta,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Long usuarioId
    ) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);
        Specification<Incidencia> spec = (root, query, cb) -> cb.conjunction();
        if (estado != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("estado"), estado));
        if (categoriaId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId));
        if (ubicacionId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("ubicacion").get("id"), ubicacionId));
        if (usuarioId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("usuarioCreador").get("id"), usuarioId));
        if (fechaCreacionDesde != null) spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaCreacion"), LocalDateTime.parse(fechaCreacionDesde)));
        if (fechaCreacionHasta != null) spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaCreacion"), LocalDateTime.parse(fechaCreacionHasta)));
        if (fechaResolucionDesde != null) spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaResolucion"), LocalDateTime.parse(fechaResolucionDesde)));
        if (fechaResolucionHasta != null) spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaResolucion"), LocalDateTime.parse(fechaResolucionHasta)));
        if (texto != null && !texto.isEmpty()) spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("descripcion")), "%" + texto.toLowerCase() + "%"));
        return incidenciaService.findAll(spec, pageable);
    }

    @GetMapping("/{id}")
    public Optional<Incidencia> getById(@PathVariable Long id) {
        return incidenciaService.findById(id);
    }

    @PostMapping
    public Incidencia create(@Valid @RequestBody Incidencia incidencia) {
        return incidenciaService.save(incidencia);
    }

    @PutMapping("/{id}/estado")
    public Incidencia cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoRequest body, Authentication authentication) {
        EstadoIncidencia nuevoEstado;
        try {
            nuevoEstado = EstadoIncidencia.valueOf(body.getEstado().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido");
        }

        String emailUsuario = authentication != null ? authentication.getName() : null;
        var usuarioActual = usuarioService.findByEmail(emailUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"));

        Incidencia incidenciaActualizada = incidenciaService.cambiarEstado(id, nuevoEstado, body.getDescripcionSolucion(), usuarioActual);
        if (incidenciaActualizada == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidencia no encontrada");
        }
        return incidenciaActualizada;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        // Filtros por fecha de creación
        Specification<Incidencia> spec = (root, query, cb) -> cb.conjunction();
        if (desde != null) spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaCreacion"), desde));
        if (hasta != null) spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaCreacion"), hasta));
        var todas = incidenciaService.findAll(spec);
        long abiertas = todas.stream().filter(i -> i.getEstado() == Incidencia.EstadoIncidencia.PENDIENTE || i.getEstado() == Incidencia.EstadoIncidencia.EN_PROCESO).count();
        long resueltas = todas.stream().filter(i -> i.getEstado() == Incidencia.EstadoIncidencia.REALIZADA).count();
        double tiempoMedio = todas.stream().filter(i -> i.getEstado() == Incidencia.EstadoIncidencia.REALIZADA && i.getFechaResolucion() != null)
            .mapToLong(i -> java.time.Duration.between(i.getFechaCreacion(), i.getFechaResolucion()).toHours()).average().orElse(0);
        var topUbicaciones = todas.stream().collect(Collectors.groupingBy(i -> i.getUbicacion().getNombre(), Collectors.counting()))
            .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(5).collect(Collectors.toList());
        var topCategorias = todas.stream().collect(Collectors.groupingBy(i -> i.getCategoria().getNombre(), Collectors.counting()))
            .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(5).collect(Collectors.toList());
        Map<String, Object> res = new HashMap<>();
        res.put("abiertas", abiertas);
        res.put("resueltas", resueltas);
        res.put("tiempoMedioHoras", tiempoMedio);
        res.put("topUbicaciones", topUbicaciones);
        res.put("topCategorias", topCategorias);
        return res;
    }
}
