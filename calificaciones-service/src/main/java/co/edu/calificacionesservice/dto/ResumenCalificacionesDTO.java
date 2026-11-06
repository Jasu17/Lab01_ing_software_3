package co.edu.calificacionesservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumenCalificacionesDTO {
    private Long matriculaId;
    private List<CalificacionDTO> calificaciones;
    private BigDecimal promedioPonderado;
    private boolean completa;

    public Long getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Long matriculaId) { this.matriculaId = matriculaId; }
    public List<CalificacionDTO> getCalificaciones() { return calificaciones; }
    public void setCalificaciones(List<CalificacionDTO> calificaciones) { this.calificaciones = calificaciones; }
    public BigDecimal getPromedioPonderado() { return promedioPonderado; }
    public void setPromedioPonderado(BigDecimal promedioPonderado) { this.promedioPonderado = promedioPonderado; }
    public boolean isCompleta() { return completa; }
    public void setCompleta(boolean completa) { this.completa = completa; }
}
