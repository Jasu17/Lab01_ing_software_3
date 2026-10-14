package co.edu.demoacademico.asignaturas.controller;

import co.edu.demoacademico.common.api.ApiResponse;
import co.edu.demoacademico.common.api.ResponseBuilder;
import co.edu.demoacademico.asignaturas.dto.AsignaturaCreateDTO;
import co.edu.demoacademico.asignaturas.dto.AsignaturaDTO;
import co.edu.demoacademico.asignaturas.handler.AsignaturaHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {

    private final AsignaturaHandler handler;

    public AsignaturaController(AsignaturaHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AsignaturaDTO>> crear(@Valid @RequestBody AsignaturaCreateDTO in) {
        return ResponseBuilder.created("Asignatura creada", handler.crear(in));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AsignaturaDTO>>> listar() {
        return ResponseBuilder.ok("OK", handler.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        handler.eliminar(id);
        return ResponseBuilder.ok("Asignatura eliminada", null);
    }
}
