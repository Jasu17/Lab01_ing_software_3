package co.edu.calificacionesservice.dto;

public class MatriculaRemotaDTO {
    private Long id;
    private Long estudianteId;
    private Long cursoId;
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
