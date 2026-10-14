package co.edu.demoacademico.controller;

import co.edu.demoacademico.api.ApiResponse;
import co.edu.demoacademico.api.ResponseBuilder;
import co.edu.demoacademico.dto.GrupoCreateDTO;
import co.edu.demoacademico.dto.GrupoDTO;
import co.edu.demoacademico.handler.GrupoHandler;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoHandler handler;

    public GrupoController(GrupoHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GrupoDTO>> crear(@Valid @RequestBody GrupoCreateDTO in) {
        return ResponseBuilder.created("Grupo creado", handler.crear(in));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GrupoDTO>>> listar(@ParameterObject Pageable pageable) {
        return ResponseBuilder.ok("OK", handler.listar(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        handler.eliminar(id);
        return ResponseBuilder.ok("Grupo eliminado", null);
    }
}
