// En: src/main/java/co/edu/demoacademico/service/EstudianteService.java

package co.edu.demoacademico.service;

import co.edu.demoacademico.model.Estudiante;
import co.edu.demoacademico.repository.EstudianteRepository;
import co.edu.demoacademico.exception.EmailDuplicadoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * CAPA DE LÓGICA DE NEGOCIO (Service)
 * ============================================================================
 * 
 * Responsabilidad de diseño:
 * - Actúa como el núcleo o cerebro del sistema.
 * - Coordina el flujo de datos entre la capa de presentación y la capa de acceso a datos.
 * - Implementa y aplica todas las reglas de negocio, validaciones de dominio y políticas del sistema.
 * - Mantiene el desacoplamiento entre las peticiones HTTP y el almacenamiento persistente.
 * 
 * Flujo de datos: HTTP Request -> Controller -> Service -> Repository -> BD
 */
@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public Estudiante crear(Estudiante estudiante) {
        // --------------------------------------------------------------------
        // ZONA DE LÓGICA DE NEGOCIO:
        // Validación de regla de negocio de dominio (email único en el sistema).
        // --------------------------------------------------------------------
        repository.findByEmail(estudiante.getEmail())
                .ifPresent(e -> {
                    throw new EmailDuplicadoException(
                            "El email '" + estudiante.getEmail() + "' ya está registrado. " +
                                    "Por favor, use un correo electrónico diferente."
                    );
                });

        // --------------------------------------------------------------------
        // ZONA DE DELEGACIÓN DE PERSISTENCIA:
        // Delegación de la persistencia a la capa de acceso a datos (Repository).
        // --------------------------------------------------------------------
        return repository.save(estudiante);
    }

    public List<Estudiante> listar() {
        return repository.findAll();
    }

    // ============================
    // NUEVO MÉTODO: Búsqueda por email
    // ZONA DE ACCESO A LA BD
    // ============================
    public Optional<Estudiante> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}