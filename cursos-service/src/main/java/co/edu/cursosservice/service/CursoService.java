package co.edu.cursosservice.service;

import co.edu.cursosservice.model.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CursoService {
    Curso crear(Curso curso);
    Curso obtenerPorId(Long id);
    Page<Curso> listar(Pageable pageable);
    Curso actualizar(Long id, Curso curso);
    void eliminar(Long id);
}
