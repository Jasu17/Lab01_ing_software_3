package co.edu.calificacionesservice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificacion", uniqueConstraints = {
        @UniqueConstraint(name = "uk_calificacion_matricula_corte", columnNames = {"matricula_id", "corte"})
})
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula_id", nullable = false)
    private Long matriculaId;

    @Column(nullable = false)
    private Integer corte;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal nota;

    @Column(nullable = false, length = 80)
    private String registradoPor;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    public Calificacion() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Long matriculaId) { this.matriculaId = matriculaId; }
    public Integer getCorte() { return corte; }
    public void setCorte(Integer corte) { this.corte = corte; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String registradoPor) { this.registradoPor = registradoPor; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
