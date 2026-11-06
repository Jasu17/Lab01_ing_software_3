package co.edu.estudiantesservice.controller;

import co.edu.estudiantesservice.api.ApiResponse;
import co.edu.estudiantesservice.api.ResponseBuilder;
import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.dto.EstudianteDTO;
import co.edu.estudiantesservice.dto.EstudianteUpdateDTO;
import co.edu.estudiantesservice.handler.EstudianteHandler;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteHandler handler;

    public EstudianteController(EstudianteHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EstudianteDTO>> crear(@Valid @RequestBody EstudianteCreateDTO in) {
        return ResponseBuilder.created("Estudiante creado", handler.crear(in));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstudianteDTO>> obtener(
            @Parameter(description = "Id del estudiante", required = true)
            @PathVariable("id") Long id) {
        return ResponseBuilder.ok("OK", handler.obtener(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EstudianteDTO>>> listar(@ParameterObject Pageable pageable) {
        return ResponseBuilder.ok("OK", handler.listar(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EstudianteDTO>> actualizar(
            @Parameter(description = "Id del estudiante", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody EstudianteUpdateDTO in) {
        return ResponseBuilder.ok("Estudiante actualizado", handler.actualizar(id, in));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(
            @Parameter(description = "Id del estudiante", required = true)
            @PathVariable("id") Long id) {
        handler.eliminar(id);
        return ResponseBuilder.ok("Estudiante eliminado", null);
    }
}
