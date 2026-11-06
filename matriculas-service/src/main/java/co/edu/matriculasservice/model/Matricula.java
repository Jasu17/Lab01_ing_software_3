package co.edu.matriculasservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long estudianteId;

    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private LocalDate fechaMatricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMatricula estado;

    public Matricula() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
    public EstadoMatricula getEstado() { return estado; }
    public void setEstado(EstadoMatricula estado) { this.estado = estado; }
}
