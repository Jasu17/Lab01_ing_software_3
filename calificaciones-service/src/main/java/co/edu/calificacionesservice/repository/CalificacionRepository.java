package co.edu.calificacionesservice.repository;

import co.edu.calificacionesservice.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    boolean existsByMatriculaIdAndCorte(Long matriculaId, Integer corte);
    List<Calificacion> findByMatriculaIdOrderByCorteAsc(Long matriculaId);
}
