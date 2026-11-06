package co.edu.cursosservice.service;

import co.edu.cursosservice.client.MatriculasClient;
import co.edu.cursosservice.dto.RemoteApiResponse;
import co.edu.cursosservice.exception.BusinessException;
import co.edu.cursosservice.exception.ConflictException;
import co.edu.cursosservice.exception.NotFoundException;
import co.edu.cursosservice.exception.RemoteServiceException;
import co.edu.cursosservice.model.Curso;
import co.edu.cursosservice.repository.CursoRepository;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository repository;
    private final MatriculasClient matriculasClient;

    public CursoServiceImpl(CursoRepository repository, MatriculasClient matriculasClient) {
        this.repository = repository;
        this.matriculasClient = matriculasClient;
    }

    @Override
    public Curso crear(Curso curso) {
        if (repository.existsByCodigo(curso.getCodigo())) {
            throw new BusinessException("Ya existe un curso con el código: " + curso.getCodigo());
        }
        return repository.save(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public Curso obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Curso> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Curso actualizar(Long id, Curso curso) {
        Curso actual = obtenerPorId(id);
        if (repository.existsByCodigoAndIdNot(curso.getCodigo(), id)) {
            throw new BusinessException("Ya existe otro curso con el código: " + curso.getCodigo());
        }
        if (curso.getCupoMaximo() < actual.getCupoMaximo()) {
            long activas = contarMatriculasActivas(id);
            if (activas > curso.getCupoMaximo()) {
                throw new BusinessException("El cupo no puede ser menor que las " + activas + " matrícula(s) activa(s) del curso");
            }
        }
        actual.setCodigo(curso.getCodigo());
        actual.setNombre(curso.getNombre());
        actual.setDescripcion(curso.getDescripcion());
        actual.setCreditos(curso.getCreditos());
        actual.setDocenteResponsable(curso.getDocenteResponsable());
        actual.setCupoMaximo(curso.getCupoMaximo());
        return repository.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerPorId(id);
        long activas = contarMatriculasActivas(id);
        if (activas > 0) {
            throw new ConflictException("No se puede eliminar: el curso tiene " + activas + " matrícula(s) activa(s)");
        }
        repository.delete(curso);
    }

    private long contarMatriculasActivas(Long cursoId) {
        try {
            RemoteApiResponse<Long> response = matriculasClient.contarActivasPorCurso(cursoId);
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new RemoteServiceException("matriculas-service no devolvió el conteo de matrículas");
            }
            return response.getData();
        } catch (FeignException ex) {
            throw new RemoteServiceException("No fue posible verificar las matrículas del curso (código remoto " + ex.status() + ")");
        }
    }
}
