package co.edu.demoacademico.programas.service;

import co.edu.demoacademico.programas.model.Programa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProgramaService {
    Programa crear(Programa p);
    Programa obtenerPorId(Long id);
    Page<Programa> listar(Pageable pageable);
    Programa actualizar(Long id, Programa p);
    void eliminar(Long id);
}
