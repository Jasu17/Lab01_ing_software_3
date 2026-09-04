package co.edu.demoacademico.handler;

import co.edu.demoacademico.dto.EstudianteCreateDTO;
import co.edu.demoacademico.dto.EstudianteDTO;
import co.edu.demoacademico.dto.EstudianteUpdateDTO;
import co.edu.demoacademico.model.Estudiante;
import co.edu.demoacademico.service.EstudianteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class EstudianteHandler {

    private final EstudianteService service;

    public EstudianteHandler(EstudianteService service) {
        this.service = service;
    }

    public EstudianteDTO crear(EstudianteCreateDTO in) {
        Estudiante entity = new Estudiante();
        entity.setNombre(in.getNombre());
        entity.setEmail(in.getEmail());

        Estudiante saved = service.crear(entity);
        return toDto(saved);
    }

    public EstudianteDTO obtener(Long id) {
        return toDto(service.obtenerPorId(id));
    }

    public Page<EstudianteDTO> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toDto);
    }

    public EstudianteDTO actualizar(Long id, EstudianteUpdateDTO in) {
        Estudiante entity = new Estudiante();
        entity.setNombre(in.getNombre());
        entity.setEmail(in.getEmail());

        Estudiante updated = service.actualizar(id, entity);
        return toDto(updated);
    }

    public void eliminar(Long id) {
        service.eliminar(id);
    }

    private EstudianteDTO toDto(Estudiante e) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setEmail(e.getEmail());
        return dto;
    }
}
