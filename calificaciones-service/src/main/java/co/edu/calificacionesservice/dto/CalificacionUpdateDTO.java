package co.edu.calificacionesservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CalificacionUpdateDTO {

    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0.0")
    @DecimalMax(value = "5.0", message = "La nota máxima es 5.0")
    @Digits(integer = 1, fraction = 1, message = "La nota admite un solo decimal")
    private BigDecimal nota;

    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
}
