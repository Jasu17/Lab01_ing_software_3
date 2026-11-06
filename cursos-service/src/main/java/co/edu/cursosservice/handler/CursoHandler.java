package co.edu.cursosservice.handler;

import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.dto.CursoDTO;
import co.edu.cursosservice.dto.CursoUpdateDTO;
import co.edu.cursosservice.model.Curso;
import co.edu.cursosservice.service.CursoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CursoHandler {

    private final CursoService service;

    public CursoHandler(CursoService service) {
        this.service = service;
    }

    public CursoDTO crear(CursoCreateDTO in) {
        Curso curso = new Curso();
        curso.setCodigo(in.getCodigo());
        curso.setNombre(in.getNombre());
        curso.setDescripcion(in.getDescripcion());
        curso.setCreditos(in.getCreditos());
        curso.setDocenteResponsable(in.getDocenteResponsable());
        curso.setCupoMaximo(in.getCupoMaximo());
        return toDto(service.crear(curso));
    }

    public CursoDTO obtener(Long id) {
        return toDto(service.obtenerPorId(id));
    }

    public Page<CursoDTO> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toDto);
    }

    public CursoDTO actualizar(Long id, CursoUpdateDTO in) {
        Curso curso = new Curso();
        curso.setCodigo(in.getCodigo());
        curso.setNombre(in.getNombre());
        curso.setDescripcion(in.getDescripcion());
        curso.setCreditos(in.getCreditos());
        curso.setDocenteResponsable(in.getDocenteResponsable());
        curso.setCupoMaximo(in.getCupoMaximo());
        return toDto(service.actualizar(id, curso));
    }

    public void eliminar(Long id) {
        service.eliminar(id);
    }

    private CursoDTO toDto(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setCodigo(curso.getCodigo());
        dto.setNombre(curso.getNombre());
        dto.setDescripcion(curso.getDescripcion());
        dto.setCreditos(curso.getCreditos());
        dto.setDocenteResponsable(curso.getDocenteResponsable());
        dto.setCupoMaximo(curso.getCupoMaximo());
        return dto;
    }
}
