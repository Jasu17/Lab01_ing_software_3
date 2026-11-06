package co.edu.cursosservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "curso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_curso_codigo", columnNames = {"codigo"})
})
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer creditos;

    @Column(nullable = false, length = 150)
    private String docenteResponsable;

    @Column(nullable = false)
    private Integer cupoMaximo;

    public Curso() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
