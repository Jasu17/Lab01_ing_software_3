package co.edu.calificacionesservice.model;

import java.math.BigDecimal;
import java.util.List;

public record ResumenCalificaciones(Long matriculaId, List<Calificacion> calificaciones, BigDecimal promedioPonderado) {

    public boolean completa() {
        return calificaciones.size() == 3;
    }
}
