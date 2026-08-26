package co.edu.demoacademico.controller;

import co.edu.demoacademico.model.Estudiante;
import co.edu.demoacademico.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * CAPA DE PRESENTACIÓN (Controller)
 * ============================================================================
 * 
 * Responsabilidad de diseño:
 * - Actúa como el punto de entrada para las peticiones HTTP externas (REST API).
 * - Mapea los endpoints (/api/estudiantes) y gestiona los métodos HTTP (GET, POST, etc.).
 * - Recibe y deserializa payloads JSON en objetos del modelo de dominio.
 * - Valida sintáctica y estructuralmente los datos de entrada mediante la anotación {@code @Valid}.
 * - Delega la ejecución de los casos de uso a la capa de servicio (EstudianteService).
 * 
 * Restricciones de diseño de capa:
 * - Esta capa NO debe contener reglas ni lógica de negocio.
 * - Esta capa NO debe interactuar ni tener acceso directo a la base de datos o repositorios.
 * 
 * Flujo de datos: HTTP Request -> Controller -> Service -> Repository -> BD
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @PostMapping
    public Estudiante crear(@Valid @RequestBody Estudiante estudiante) {
        return service.crear(estudiante);
    }

    @GetMapping
    public List<Estudiante> listar() {
        return service.listar();
    }

    @GetMapping("/buscar")
    public Optional<Estudiante> buscarPorEmail(@RequestParam String email) {
        return service.buscarPorEmail(email);
    }
}
