// En: src/main/java/co/edu/demoacademico/service/EstudianteService.java

package co.edu.demoacademico.service;

import co.edu.demoacademico.model.Estudiante;
import co.edu.demoacademico.repository.EstudianteRepository;
import co.edu.demoacademico.exception.EmailDuplicadoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public Estudiante crear(Estudiante estudiante) {
        repository.findByEmail(estudiante.getEmail())
                .ifPresent(e -> {
                    throw new EmailDuplicadoException(
                            "El email '" + estudiante.getEmail() + "' ya está registrado. " +
                                    "Por favor, use un correo electrónico diferente."
                    );
                });
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