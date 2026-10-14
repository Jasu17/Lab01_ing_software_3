package co.edu.demoacademico.grupos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GrupoCreateDTO {
    @NotBlank(message = "codigoGrupo es obligatorio")
    private String codigoGrupo;

    @NotNull(message = "cupoMax es obligatorio")
    @Min(value = 1, message = "cupoMax mínimo 1")
    private Integer cupoMax;

    @NotNull(message = "asignaturaId es obligatorio")
    private Long asignaturaId;

    public String getCodigoGrupo() { return codigoGrupo; }
    public void setCodigoGrupo(String codigoGrupo) { this.codigoGrupo = codigoGrupo; }

    public Integer getCupoMax() { return cupoMax; }
    public void setCupoMax(Integer cupoMax) { this.cupoMax = cupoMax; }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }
}
