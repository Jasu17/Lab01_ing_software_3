package co.edu.calificacionesservice.controller;

import co.edu.calificacionesservice.api.ApiResponse;
import co.edu.calificacionesservice.api.ResponseBuilder;
import co.edu.calificacionesservice.dto.CalificacionCreateDTO;
import co.edu.calificacionesservice.dto.CalificacionDTO;
import co.edu.calificacionesservice.dto.CalificacionUpdateDTO;
import co.edu.calificacionesservice.dto.ResumenCalificacionesDTO;
import co.edu.calificacionesservice.handler.CalificacionHandler;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    private final CalificacionHandler handler;

    public CalificacionController(CalificacionHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CalificacionDTO>> registrar(
            @Valid @RequestBody CalificacionCreateDTO in,
            @Parameter(hidden = true) Authentication authentication) {
        return ResponseBuilder.created("Calificación registrada", handler.registrar(in, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CalificacionDTO>> actualizar(
            @Parameter(description = "Id de la calificación", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody CalificacionUpdateDTO in,
            @Parameter(hidden = true) Authentication authentication) {
        return ResponseBuilder.ok("Calificación actualizada", handler.actualizar(id, in, authentication.getName()));
    }

    @GetMapping("/matricula/{matriculaId}")
    public ResponseEntity<ApiResponse<ResumenCalificacionesDTO>> resumen(
            @Parameter(description = "Id de la matrícula", required = true)
            @PathVariable("matriculaId") Long matriculaId) {
        return ResponseBuilder.ok("OK", handler.resumen(matriculaId));
    }

    @GetMapping("/mias")
    public ResponseEntity<ApiResponse<List<ResumenCalificacionesDTO>>> misCalificaciones() {
        return ResponseBuilder.ok("OK", handler.misCalificaciones());
    }
}
