package co.edu.estudiantesservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estudiante", uniqueConstraints = {
        @UniqueConstraint(name = "uk_estudiante_email", columnNames = {"email"})
})
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String apellido;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(nullable = false)
    private Integer edad;

    public Estudiante() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
}
