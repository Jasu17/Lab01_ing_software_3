package co.edu.matriculasservice.repository;

import co.edu.matriculasservice.model.EstadoMatricula;
import co.edu.matriculasservice.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByEstudianteIdAndCursoIdAndEstado(Long estudianteId, Long cursoId, EstadoMatricula estado);
    long countByCursoIdAndEstado(Long cursoId, EstadoMatricula estado);
    long countByEstudianteIdAndEstado(Long estudianteId, EstadoMatricula estado);
    List<Matricula> findByEstudianteIdOrderByIdDesc(Long estudianteId);
}
