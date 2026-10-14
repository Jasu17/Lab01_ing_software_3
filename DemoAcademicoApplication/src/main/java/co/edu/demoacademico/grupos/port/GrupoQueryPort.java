package co.edu.demoacademico.grupos.port;

import co.edu.demoacademico.grupos.model.Grupo;

public interface GrupoQueryPort {
    Grupo obtenerPorId(Long id);
}
