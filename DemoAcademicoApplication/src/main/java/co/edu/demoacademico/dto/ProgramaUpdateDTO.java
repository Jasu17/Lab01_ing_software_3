package co.edu.demoacademico.dto;

import jakarta.validation.constraints.NotBlank;

public class ProgramaUpdateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
