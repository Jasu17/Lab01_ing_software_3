package co.edu.calificacionesservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CalificacionCreateDTO {

    @NotNull(message = "La matrícula es obligatoria")
    private Long matriculaId;

    @NotNull(message = "El corte es obligatorio")
    @Min(value = 1, message = "El corte debe ser 1, 2 o 3")
    @Max(value = 3, message = "El corte debe ser 1, 2 o 3")
    private Integer corte;

    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0.0")
    @DecimalMax(value = "5.0", message = "La nota máxima es 5.0")
    @Digits(integer = 1, fraction = 1, message = "La nota admite un solo decimal")
    private BigDecimal nota;

    public Long getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Long matriculaId) { this.matriculaId = matriculaId; }
    public Integer getCorte() { return corte; }
    public void setCorte(Integer corte) { this.corte = corte; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
}
