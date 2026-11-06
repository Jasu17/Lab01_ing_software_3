package co.edu.calificacionesservice.service;

import co.edu.calificacionesservice.model.Calificacion;
import co.edu.calificacionesservice.model.ResumenCalificaciones;

import java.math.BigDecimal;
import java.util.List;

public interface CalificacionService {
    Calificacion registrar(Long matriculaId, Integer corte, BigDecimal nota, String usuario);
    Calificacion actualizar(Long id, BigDecimal nota, String usuario);
    ResumenCalificaciones resumen(Long matriculaId);
    List<ResumenCalificaciones> misCalificaciones();
}
