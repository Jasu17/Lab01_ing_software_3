package co.edu.demoacademico.handler;

import co.edu.demoacademico.dto.GrupoCreateDTO;
import co.edu.demoacademico.dto.GrupoDTO;
import co.edu.demoacademico.model.Asignatura;
import co.edu.demoacademico.model.Grupo;
import co.edu.demoacademico.service.AsignaturaService;
import co.edu.demoacademico.service.GrupoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class GrupoHandler {

    private final GrupoService grupoService;
    private final AsignaturaService asignaturaService;

    public GrupoHandler(GrupoService grupoService, AsignaturaService asignaturaService) {
        this.grupoService = grupoService;
        this.asignaturaService = asignaturaService;
    }

    public GrupoDTO crear(GrupoCreateDTO in) {
        Asignatura a = asignaturaService.obtenerPorId(in.getAsignaturaId());

        Grupo g = new Grupo();
        g.setCodigoGrupo(in.getCodigoGrupo());
        g.setCupoMax(in.getCupoMax());
        g.setAsignatura(a);

        return toDto(grupoService.crear(g));
    }

    public Page<GrupoDTO> listar(Pageable pageable) {
        return grupoService.listar(pageable).map(this::toDto);
    }

    public void eliminar(Long id) {
        grupoService.eliminar(id);
    }

    private GrupoDTO toDto(Grupo g) {
        GrupoDTO dto = new GrupoDTO();
        dto.setId(g.getId());
        dto.setCodigoGrupo(g.getCodigoGrupo());
        dto.setCupoMax(g.getCupoMax());
        dto.setAsignaturaId(g.getAsignatura().getId());
        dto.setAsignaturaNombre(g.getAsignatura().getNombre());
        return dto;
    }
}
