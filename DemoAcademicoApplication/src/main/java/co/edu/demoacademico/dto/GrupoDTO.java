package co.edu.demoacademico.dto;

public class GrupoDTO {
    private Long id;
    private String codigoGrupo;
    private Integer cupoMax;
    private Long asignaturaId;
    private String asignaturaNombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoGrupo() { return codigoGrupo; }
    public void setCodigoGrupo(String codigoGrupo) { this.codigoGrupo = codigoGrupo; }

    public Integer getCupoMax() { return cupoMax; }
    public void setCupoMax(Integer cupoMax) { this.cupoMax = cupoMax; }

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getAsignaturaNombre() { return asignaturaNombre; }
    public void setAsignaturaNombre(String asignaturaNombre) { this.asignaturaNombre = asignaturaNombre; }
}
