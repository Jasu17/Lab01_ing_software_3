package co.edu.matriculasservice.handler;

import co.edu.matriculasservice.dto.MatriculaCreateDTO;
import co.edu.matriculasservice.dto.MatriculaDTO;
import co.edu.matriculasservice.model.Matricula;
import co.edu.matriculasservice.service.MatriculaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatriculaHandler {

    private final MatriculaService service;

    public MatriculaHandler(MatriculaService service) {
        this.service = service;
    }

    public Page<MatriculaDTO> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toDto);
    }

    public MatriculaDTO obtener(Long id) {
        return toDto(service.obtenerPorId(id));
    }

    public MatriculaDTO registrar(MatriculaCreateDTO in) {
        return toDto(service.registrar(in.getEstudianteId(), in.getCursoId()));
    }

    public MatriculaDTO anular(Long id) {
        return toDto(service.anular(id));
    }

    public long contarActivas(Long estudianteId, Long cursoId) {
        return service.contarActivas(estudianteId, cursoId);
    }

    public List<MatriculaDTO> listarPorEstudiante(Long estudianteId) {
        return service.listarPorEstudiante(estudianteId).stream().map(this::toDto).toList();
    }

    private MatriculaDTO toDto(Matricula matricula) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(matricula.getId());
        dto.setEstudianteId(matricula.getEstudianteId());
        dto.setCursoId(matricula.getCursoId());
        dto.setFechaMatricula(matricula.getFechaMatricula());
        dto.setEstado(matricula.getEstado().name());
        return dto;
    }
}
