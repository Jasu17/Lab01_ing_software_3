package co.edu.matriculasservice.controller;

import co.edu.matriculasservice.api.ApiResponse;
import co.edu.matriculasservice.api.ResponseBuilder;
import co.edu.matriculasservice.dto.MatriculaCreateDTO;
import co.edu.matriculasservice.dto.MatriculaDTO;
import co.edu.matriculasservice.handler.MatriculaHandler;
import co.edu.matriculasservice.security.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaHandler handler;

    public MatriculaController(MatriculaHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MatriculaDTO>> registrar(@Valid @RequestBody MatriculaCreateDTO in) {
        return ResponseBuilder.created("Matrícula registrada", handler.registrar(in));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MatriculaDTO>> obtener(
            @Parameter(description = "Id de la matrícula", required = true)
            @PathVariable("id") Long id) {
        return ResponseBuilder.ok("OK", handler.obtener(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MatriculaDTO>>> listar(@ParameterObject Pageable pageable) {
        return ResponseBuilder.ok("OK", handler.listar(pageable));
    }

    @GetMapping("/activas/conteo")
    public ResponseEntity<ApiResponse<Long>> contarActivas(
            @RequestParam(value = "estudianteId", required = false) Long estudianteId,
            @RequestParam(value = "cursoId", required = false) Long cursoId) {
        return ResponseBuilder.ok("OK", handler.contarActivas(estudianteId, cursoId));
    }

    @GetMapping("/mias")
    public ResponseEntity<ApiResponse<List<MatriculaDTO>>> misMatriculas(
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
        if (usuario == null || usuario.estudianteId() == null) {
            throw new AccessDeniedException("El usuario no tiene un estudiante asociado");
        }
        return ResponseBuilder.ok("OK", handler.listarPorEstudiante(usuario.estudianteId()));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<ApiResponse<MatriculaDTO>> anular(
            @Parameter(description = "Id de la matrícula", required = true)
            @PathVariable("id") Long id) {
        return ResponseBuilder.ok("Matrícula anulada", handler.anular(id));
    }
}
