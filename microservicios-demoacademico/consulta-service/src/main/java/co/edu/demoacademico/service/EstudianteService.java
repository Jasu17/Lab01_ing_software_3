package co.edu.demoacademico.service;

import co.edu.demoacademico.model.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstudianteService {
    Estudiante crear(Estudiante estudiante);
    Estudiante obtenerPorId(Long id);
    Page<Estudiante> listar(Pageable pageable);
    Estudiante actualizar(Long id, Estudiante estudiante);
    void eliminar(Long id);
}