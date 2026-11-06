package co.edu.estudiantesservice.handler;

import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.dto.EstudianteDTO;
import co.edu.estudiantesservice.dto.EstudianteUpdateDTO;
import co.edu.estudiantesservice.model.Estudiante;
import co.edu.estudiantesservice.service.EstudianteService;
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
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(in.getNombre());
        estudiante.setApellido(in.getApellido());
        estudiante.setEmail(in.getEmail());
        estudiante.setEdad(in.getEdad());
        return toDto(service.crear(estudiante));
    }

    public EstudianteDTO obtener(Long id) {
        return toDto(service.obtenerPorId(id));
    }

    public Page<EstudianteDTO> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toDto);
    }

    public EstudianteDTO actualizar(Long id, EstudianteUpdateDTO in) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(in.getNombre());
        estudiante.setApellido(in.getApellido());
        estudiante.setEmail(in.getEmail());
        estudiante.setEdad(in.getEdad());
        return toDto(service.actualizar(id, estudiante));
    }

    public void eliminar(Long id) {
        service.eliminar(id);
    }

    private EstudianteDTO toDto(Estudiante estudiante) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getId());
        dto.setNombre(estudiante.getNombre());
        dto.setApellido(estudiante.getApellido());
        dto.setEmail(estudiante.getEmail());
        dto.setEdad(estudiante.getEdad());
        return dto;
    }
}
