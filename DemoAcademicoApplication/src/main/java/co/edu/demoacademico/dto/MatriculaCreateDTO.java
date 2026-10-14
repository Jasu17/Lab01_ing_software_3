package co.edu.demoacademico.dto;

import jakarta.validation.constraints.NotNull;

public class MatriculaCreateDTO {
    @NotNull(message = "estudianteId es obligatorio")
    private Long estudianteId;

    @NotNull(message = "grupoId es obligatorio")
    private Long grupoId;

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
}
