package co.edu.demoacademico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class EstudianteCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El Email es obligatorio")
    @Email(message = "Formato de Email inválido")
    private String email;

    public EstudianteCreateDTO() {}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre; }

    public String getEmail() { return email;}
    public void setEmail(String email) {this.email = email;}
}
