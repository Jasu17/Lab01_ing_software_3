package co.edu.demoacademico.programas.handler;

import co.edu.demoacademico.programas.dto.ProgramaCreateDTO;
import co.edu.demoacademico.programas.dto.ProgramaDTO;
import co.edu.demoacademico.programas.dto.ProgramaUpdateDTO;
import co.edu.demoacademico.programas.model.Programa;
import co.edu.demoacademico.programas.service.ProgramaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ProgramaHandler {

    private final ProgramaService service;

    public ProgramaHandler(ProgramaService service) {
        this.service = service;
    }

    public ProgramaDTO crear(ProgramaCreateDTO in) {
        Programa p = new Programa();
        p.setCodigo(in.getCodigo());
        p.setNombre(in.getNombre());
        return toDto(service.crear(p));
    }

    public ProgramaDTO obtener(Long id) {
        return toDto(service.obtenerPorId(id));
    }

    public Page<ProgramaDTO> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toDto);
    }

    public ProgramaDTO actualizar(Long id, ProgramaUpdateDTO in) {
        Programa p = new Programa();
        p.setNombre(in.getNombre());
        return toDto(service.actualizar(id, p));
    }

    public void eliminar(Long id) {
        service.eliminar(id);
    }

    private ProgramaDTO toDto(Programa p) {
        ProgramaDTO dto = new ProgramaDTO();
        dto.setId(p.getId());
        dto.setCodigo(p.getCodigo());
        dto.setNombre(p.getNombre());
        return dto;
    }
}
