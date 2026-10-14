package co.edu.demoacademico.handler;

import co.edu.demoacademico.dto.MatriculaCreateDTO;
import co.edu.demoacademico.dto.MatriculaDTO;
import co.edu.demoacademico.model.Matricula;
import co.edu.demoacademico.service.MatriculaService;
import org.springframework.stereotype.Component;

@Component
public class MatriculaHandler {

    private final MatriculaService service;

    public MatriculaHandler(MatriculaService service) {
        this.service = service;
    }

    public MatriculaDTO matricular(MatriculaCreateDTO in) {
        Matricula m = service.matricular(in.getEstudianteId(), in.getGrupoId());
        return toDto(m);
    }

    private MatriculaDTO toDto(Matricula m) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(m.getId());
        dto.setEstudianteId(m.getEstudiante().getId());
        dto.setEstudianteNombre(m.getEstudiante().getNombre());
        dto.setGrupoId(m.getGrupo().getId());
        dto.setCodigoGrupo(m.getGrupo().getCodigoGrupo());
        dto.setFechaRegistro(m.getFechaRegistro());
        return dto;
    }
}
