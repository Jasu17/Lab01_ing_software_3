package co.edu.matriculasservice.service;

import co.edu.matriculasservice.client.CursosClient;
import co.edu.matriculasservice.client.EstudiantesClient;
import co.edu.matriculasservice.dto.CursoRemotoDTO;
import co.edu.matriculasservice.dto.RemoteApiResponse;
import co.edu.matriculasservice.exception.BusinessException;
import co.edu.matriculasservice.exception.NotFoundException;
import co.edu.matriculasservice.exception.RemoteServiceException;
import co.edu.matriculasservice.model.EstadoMatricula;
import co.edu.matriculasservice.model.Matricula;
import co.edu.matriculasservice.repository.MatriculaRepository;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository repository;
    private final EstudiantesClient estudiantesClient;
    private final CursosClient cursosClient;

    public MatriculaServiceImpl(MatriculaRepository repository,
                                EstudiantesClient estudiantesClient,
                                CursosClient cursosClient) {
        this.repository = repository;
        this.estudiantesClient = estudiantesClient;
        this.cursosClient = cursosClient;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Matricula> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Matricula obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Matrícula no encontrada: " + id));
    }

    @Override
    public Matricula registrar(Long estudianteId, Long cursoId) {
        obtenerRemoto("estudiante", () -> estudiantesClient.buscarPorId(estudianteId));
        CursoRemotoDTO curso = obtenerRemoto("curso", () -> cursosClient.buscarPorId(cursoId));

        if (repository.existsByEstudianteIdAndCursoIdAndEstado(estudianteId, cursoId, EstadoMatricula.ACTIVA)) {
            throw new BusinessException("Ya existe una matrícula activa para el estudiante y curso indicados");
        }

        long activas = repository.countByCursoIdAndEstado(cursoId, EstadoMatricula.ACTIVA);
        if (curso.getCupoMaximo() != null && activas >= curso.getCupoMaximo()) {
            throw new BusinessException("El curso no tiene cupos disponibles (" + activas + "/" + curso.getCupoMaximo() + ")");
        }

        Matricula matricula = new Matricula();
        matricula.setEstudianteId(estudianteId);
        matricula.setCursoId(cursoId);
        matricula.setFechaMatricula(LocalDate.now());
        matricula.setEstado(EstadoMatricula.ACTIVA);
        return repository.save(matricula);
    }

    @Override
    public Matricula anular(Long id) {
        Matricula matricula = obtenerPorId(id);
        if (matricula.getEstado() == EstadoMatricula.ANULADA) {
            throw new BusinessException("La matrícula ya se encuentra anulada");
        }
        matricula.setEstado(EstadoMatricula.ANULADA);
        return repository.save(matricula);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivas(Long estudianteId, Long cursoId) {
        if ((estudianteId == null) == (cursoId == null)) {
            throw new BusinessException("Envíe exactamente uno de los parámetros: estudianteId o cursoId");
        }
        return estudianteId != null
                ? repository.countByEstudianteIdAndEstado(estudianteId, EstadoMatricula.ACTIVA)
                : repository.countByCursoIdAndEstado(cursoId, EstadoMatricula.ACTIVA);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Matricula> listarPorEstudiante(Long estudianteId) {
        return repository.findByEstudianteIdOrderByIdDesc(estudianteId);
    }

    private <T> T obtenerRemoto(String recurso, Supplier<RemoteApiResponse<T>> llamada) {
        try {
            RemoteApiResponse<T> response = llamada.get();
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new BusinessException("El " + recurso + " indicado no existe");
            }
            return response.getData();
        } catch (FeignException.NotFound ex) {
            throw new BusinessException("El " + recurso + " indicado no existe");
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            throw new RemoteServiceException("No fue posible validar el " + recurso + ": el servicio remoto rechazó el token");
        } catch (FeignException ex) {
            throw new RemoteServiceException("No fue posible consultar el " + recurso + " (código remoto " + ex.status() + ")");
        }
    }
}
