package co.edu.cursosservice.controller;

import co.edu.cursosservice.api.ApiResponse;
import co.edu.cursosservice.api.ResponseBuilder;
import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.dto.CursoDTO;
import co.edu.cursosservice.dto.CursoUpdateDTO;
import co.edu.cursosservice.handler.CursoHandler;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoHandler handler;

    public CursoController(CursoHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CursoDTO>> crear(@Valid @RequestBody CursoCreateDTO in) {
        return ResponseBuilder.created("Curso creado", handler.crear(in));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoDTO>> obtener(
            @Parameter(description = "Id del curso", required = true)
            @PathVariable("id") Long id) {
        return ResponseBuilder.ok("OK", handler.obtener(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CursoDTO>>> listar(@ParameterObject Pageable pageable) {
        return ResponseBuilder.ok("OK", handler.listar(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoDTO>> actualizar(
            @Parameter(description = "Id del curso", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody CursoUpdateDTO in) {
        return ResponseBuilder.ok("Curso actualizado", handler.actualizar(id, in));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(
            @Parameter(description = "Id del curso", required = true)
            @PathVariable("id") Long id) {
        handler.eliminar(id);
        return ResponseBuilder.ok("Curso eliminado", null);
    }
}
