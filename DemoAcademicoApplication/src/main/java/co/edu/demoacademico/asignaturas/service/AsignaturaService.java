package co.edu.demoacademico.asignaturas.service;

import co.edu.demoacademico.asignaturas.model.Asignatura;

import java.util.List;

public interface AsignaturaService {
    Asignatura crear(Asignatura a);
    Asignatura obtenerPorId(Long id);
    List<Asignatura> listar();
    void eliminar(Long id);
}
