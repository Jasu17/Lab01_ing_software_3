package co.edu.estudiantesservice.service;

import co.edu.estudiantesservice.client.MatriculasClient;
import co.edu.estudiantesservice.dto.RemoteApiResponse;
import co.edu.estudiantesservice.exception.BusinessException;
import co.edu.estudiantesservice.exception.ConflictException;
import co.edu.estudiantesservice.exception.NotFoundException;
import co.edu.estudiantesservice.exception.RemoteServiceException;
import co.edu.estudiantesservice.model.Estudiante;
import co.edu.estudiantesservice.repository.EstudianteRepository;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository repository;
    private final MatriculasClient matriculasClient;

    public EstudianteServiceImpl(EstudianteRepository repository, MatriculasClient matriculasClient) {
        this.repository = repository;
        this.matriculasClient = matriculasClient;
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        if (repository.existsByEmail(estudiante.getEmail())) {
            throw new BusinessException("Ya existe un estudiante con el email: " + estudiante.getEmail());
        }
        return repository.save(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Estudiante> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Estudiante actualizar(Long id, Estudiante estudiante) {
        Estudiante actual = obtenerPorId(id);
        if (repository.existsByEmailAndIdNot(estudiante.getEmail(), id)) {
            throw new BusinessException("Ya existe otro estudiante con el email: " + estudiante.getEmail());
        }
        actual.setNombre(estudiante.getNombre());
        actual.setApellido(estudiante.getApellido());
        actual.setEmail(estudiante.getEmail());
        actual.setEdad(estudiante.getEdad());
        return repository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Estudiante estudiante = obtenerPorId(id);
        long activas = contarMatriculasActivas(id);
        if (activas > 0) {
            throw new ConflictException("No se puede eliminar: el estudiante tiene " + activas + " matrícula(s) activa(s)");
        }
        repository.delete(estudiante);
    }

    private long contarMatriculasActivas(Long estudianteId) {
        try {
            RemoteApiResponse<Long> response = matriculasClient.contarActivasPorEstudiante(estudianteId);
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new RemoteServiceException("matriculas-service no devolvió el conteo de matrículas");
            }
            return response.getData();
        } catch (FeignException ex) {
            throw new RemoteServiceException("No fue posible verificar las matrículas del estudiante (código remoto " + ex.status() + ")");
        }
    }
}
