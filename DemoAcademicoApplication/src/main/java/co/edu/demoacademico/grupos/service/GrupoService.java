package co.edu.demoacademico.grupos.service;

import co.edu.demoacademico.grupos.model.Grupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GrupoService {
    Grupo crear(Grupo g);
    Grupo obtenerPorId(Long id);
    Page<Grupo> listar(Pageable pageable);
    void eliminar(Long id);
}
