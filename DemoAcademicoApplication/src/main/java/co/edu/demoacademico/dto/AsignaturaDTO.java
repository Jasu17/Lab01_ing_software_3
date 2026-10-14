package co.edu.demoacademico.dto;

public class AsignaturaDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Integer creditos;
    private Long programaId;
    private String programaNombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }

    public Long getProgramaId() { return programaId; }
    public void setProgramaId(Long programaId) { this.programaId = programaId; }

    public String getProgramaNombre() { return programaNombre; }
    public void setProgramaNombre(String programaNombre) { this.programaNombre = programaNombre; }
}
