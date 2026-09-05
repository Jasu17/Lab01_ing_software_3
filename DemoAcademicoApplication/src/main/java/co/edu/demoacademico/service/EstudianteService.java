package co.edu.demoacademico.service;

import co.edu.demoacademico.model.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface EstudianteService {

    Estudiante crear(Estudiante e);

    Estudiante obtenerPorId(Long id);

    Page<Estudiante> listar(Pageable pageable);

    Estudiante actualizar(Long id, Estudiante e);

    Optional<Estudiante> buscarPorEmail(String email);

    void eliminar(Long id);
}