package co.edu.demoacademico.matriculas.service;

import co.edu.demoacademico.matriculas.model.Matricula;

public interface MatriculaService {
    Matricula matricular(Long estudianteId, Long grupoId);
}
