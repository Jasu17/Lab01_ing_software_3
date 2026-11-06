package co.edu.matriculasservice.service;

import co.edu.matriculasservice.model.Matricula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatriculaService {
    Page<Matricula> listar(Pageable pageable);
    Matricula obtenerPorId(Long id);
    Matricula registrar(Long estudianteId, Long cursoId);
    Matricula anular(Long id);
    long contarActivas(Long estudianteId, Long cursoId);
    List<Matricula> listarPorEstudiante(Long estudianteId);
}
