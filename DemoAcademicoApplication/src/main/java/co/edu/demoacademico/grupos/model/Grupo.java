package co.edu.demoacademico.grupos.model;

import co.edu.demoacademico.asignaturas.model.Asignatura;
import jakarta.persistence.*;

@Entity
@Table(name = "grupo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_grupo_codigo", columnNames = {"codigo_grupo"})
})
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_grupo", nullable = false, length = 30)
    private String codigoGrupo;

    @Column(nullable = false)
    private Integer cupoMax;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_grupo_asignatura"))
    private Asignatura asignatura;

    public Grupo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoGrupo() { return codigoGrupo; }
    public void setCodigoGrupo(String codigoGrupo) { this.codigoGrupo = codigoGrupo; }

    public Integer getCupoMax() { return cupoMax; }
    public void setCupoMax(Integer cupoMax) { this.cupoMax = cupoMax; }

    public Asignatura getAsignatura() { return asignatura; }
    public void setAsignatura(Asignatura asignatura) { this.asignatura = asignatura; }
}
