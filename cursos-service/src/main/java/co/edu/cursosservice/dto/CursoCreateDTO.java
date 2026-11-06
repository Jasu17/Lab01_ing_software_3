package co.edu.cursosservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CursoCreateDTO {

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código admite máximo 20 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Size(max = 500, message = "La descripción admite máximo 500 caracteres")
    private String descripcion;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos mínimos son 1")
    @Max(value = 10, message = "Los créditos máximos son 10")
    private Integer creditos;

    @NotBlank(message = "El docente responsable es obligatorio")
    private String docenteResponsable;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo mínimo es 1")
    @Max(value = 60, message = "El cupo máximo es 60")
    private Integer cupoMaximo;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
    public String getDocenteResponsable() { return docenteResponsable; }
    public void setDocenteResponsable(String docenteResponsable) { this.docenteResponsable = docenteResponsable; }
    public Integer getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }
}
