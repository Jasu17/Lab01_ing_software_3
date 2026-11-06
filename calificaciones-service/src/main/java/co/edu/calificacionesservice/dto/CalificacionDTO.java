package co.edu.calificacionesservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CalificacionDTO {
    private Long id;
    private Long matriculaId;
    private Integer corte;
    private BigDecimal nota;
    private String registradoPor;
    private LocalDateTime fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Long matriculaId) { this.matriculaId = matriculaId; }
    public Integer getCorte() { return corte; }
    public void setCorte(Integer corte) { this.corte = corte; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String registradoPor) { this.registradoPor = registradoPor; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
