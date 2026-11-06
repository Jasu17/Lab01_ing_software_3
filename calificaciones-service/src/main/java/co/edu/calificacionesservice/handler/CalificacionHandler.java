package co.edu.calificacionesservice.handler;

import co.edu.calificacionesservice.dto.CalificacionCreateDTO;
import co.edu.calificacionesservice.dto.CalificacionDTO;
import co.edu.calificacionesservice.dto.CalificacionUpdateDTO;
import co.edu.calificacionesservice.dto.ResumenCalificacionesDTO;
import co.edu.calificacionesservice.model.Calificacion;
import co.edu.calificacionesservice.model.ResumenCalificaciones;
import co.edu.calificacionesservice.service.CalificacionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalificacionHandler {

    private final CalificacionService service;

    public CalificacionHandler(CalificacionService service) {
        this.service = service;
    }

    public CalificacionDTO registrar(CalificacionCreateDTO in, String usuario) {
        return toDto(service.registrar(in.getMatriculaId(), in.getCorte(), in.getNota(), usuario));
    }

    public CalificacionDTO actualizar(Long id, CalificacionUpdateDTO in, String usuario) {
        return toDto(service.actualizar(id, in.getNota(), usuario));
    }

    public ResumenCalificacionesDTO resumen(Long matriculaId) {
        return toDto(service.resumen(matriculaId));
    }

    public List<ResumenCalificacionesDTO> misCalificaciones() {
        return service.misCalificaciones().stream().map(this::toDto).toList();
    }

    private CalificacionDTO toDto(Calificacion calificacion) {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(calificacion.getId());
        dto.setMatriculaId(calificacion.getMatriculaId());
        dto.setCorte(calificacion.getCorte());
        dto.setNota(calificacion.getNota());
        dto.setRegistradoPor(calificacion.getRegistradoPor());
        dto.setFechaRegistro(calificacion.getFechaRegistro());
        return dto;
    }

    private ResumenCalificacionesDTO toDto(ResumenCalificaciones resumen) {
        ResumenCalificacionesDTO dto = new ResumenCalificacionesDTO();
        dto.setMatriculaId(resumen.matriculaId());
        dto.setCalificaciones(resumen.calificaciones().stream().map(this::toDto).toList());
        dto.setPromedioPonderado(resumen.promedioPonderado());
        dto.setCompleta(resumen.completa());
        return dto;
    }
}
