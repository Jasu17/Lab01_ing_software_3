package co.edu.demoacademico.programas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "programa", uniqueConstraints = {
        @UniqueConstraint(name = "uk_programa_codigo", columnNames = {"codigo"})
})
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    public Programa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
