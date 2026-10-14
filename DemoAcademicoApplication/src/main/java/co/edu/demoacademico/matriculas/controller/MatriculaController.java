package co.edu.demoacademico.matriculas.controller;

import co.edu.demoacademico.common.api.ApiResponse;
import co.edu.demoacademico.common.api.ResponseBuilder;
import co.edu.demoacademico.matriculas.dto.MatriculaCreateDTO;
import co.edu.demoacademico.matriculas.dto.MatriculaDTO;
import co.edu.demoacademico.matriculas.handler.MatriculaHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaHandler handler;

    public MatriculaController(MatriculaHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MatriculaDTO>> matricular(@Valid @RequestBody MatriculaCreateDTO in) {
        return ResponseBuilder.created("Matrícula registrada", handler.matricular(in));
    }
}
