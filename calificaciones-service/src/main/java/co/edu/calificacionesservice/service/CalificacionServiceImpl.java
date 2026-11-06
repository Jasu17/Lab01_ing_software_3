package co.edu.calificacionesservice.service;

import co.edu.calificacionesservice.client.MatriculasClient;
import co.edu.calificacionesservice.dto.MatriculaRemotaDTO;
import co.edu.calificacionesservice.dto.RemoteApiResponse;
import co.edu.calificacionesservice.exception.BusinessException;
import co.edu.calificacionesservice.exception.NotFoundException;
import co.edu.calificacionesservice.exception.RemoteServiceException;
import co.edu.calificacionesservice.model.Calificacion;
import co.edu.calificacionesservice.model.ResumenCalificaciones;
import co.edu.calificacionesservice.repository.CalificacionRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CalificacionServiceImpl implements CalificacionService {

    /** Peso de cada corte en la nota final. */
    static final Map<Integer, BigDecimal> PESOS = Map.of(
            1, new BigDecimal("0.30"),
            2, new BigDecimal("0.30"),
            3, new BigDecimal("0.40"));

    private final CalificacionRepository repository;
    private final MatriculasClient matriculasClient;

    public CalificacionServiceImpl(CalificacionRepository repository, MatriculasClient matriculasClient) {
        this.repository = repository;
        this.matriculasClient = matriculasClient;
    }

    @Override
    public Calificacion registrar(Long matriculaId, Integer corte, BigDecimal nota, String usuario) {
        verificarMatriculaActiva(matriculaId);
        if (repository.existsByMatriculaIdAndCorte(matriculaId, corte)) {
            throw new BusinessException("Ya existe una nota para el corte " + corte + " de esta matrícula; use PUT para corregirla");
        }
        Calificacion calificacion = new Calificacion();
        calificacion.setMatriculaId(matriculaId);
        calificacion.setCorte(corte);
        calificacion.setNota(nota);
        calificacion.setRegistradoPor(usuario);
        calificacion.setFechaRegistro(LocalDateTime.now());
        return repository.save(calificacion);
    }

    @Override
    public Calificacion actualizar(Long id, BigDecimal nota, String usuario) {
        Calificacion calificacion = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Calificación no encontrada: " + id));
        verificarMatriculaActiva(calificacion.getMatriculaId());
        calificacion.setNota(nota);
        calificacion.setRegistradoPor(usuario);
        calificacion.setFechaRegistro(LocalDateTime.now());
        return repository.save(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenCalificaciones resumen(Long matriculaId) {
        obtenerMatricula(matriculaId);
        return resumenLocal(matriculaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenCalificaciones> misCalificaciones() {
        List<MatriculaRemotaDTO> matriculas;
        try {
            RemoteApiResponse<List<MatriculaRemotaDTO>> response = matriculasClient.misMatriculas();
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new RemoteServiceException("matriculas-service no devolvió las matrículas del estudiante");
            }
            matriculas = response.getData();
        } catch (FeignException ex) {
            throw new RemoteServiceException("No fue posible consultar las matrículas del estudiante (código remoto " + ex.status() + ")");
        }
        return matriculas.stream().map(m -> resumenLocal(m.getId())).toList();
    }

    /**
     * Promedio ponderado sobre los cortes registrados: se divide por la suma de sus pesos.
     * Con los tres cortes, es la nota definitiva.
     */
    static BigDecimal calcularPromedio(List<Calificacion> calificaciones) {
        if (calificaciones.isEmpty()) {
            return null;
        }
        BigDecimal suma = BigDecimal.ZERO;
        BigDecimal pesos = BigDecimal.ZERO;
        for (Calificacion c : calificaciones) {
            BigDecimal peso = PESOS.get(c.getCorte());
            suma = suma.add(c.getNota().multiply(peso));
            pesos = pesos.add(peso);
        }
        return suma.divide(pesos, 2, RoundingMode.HALF_UP);
    }

    private ResumenCalificaciones resumenLocal(Long matriculaId) {
        List<Calificacion> calificaciones = repository.findByMatriculaIdOrderByCorteAsc(matriculaId);
        return new ResumenCalificaciones(matriculaId, calificaciones, calcularPromedio(calificaciones));
    }

    private void verificarMatriculaActiva(Long matriculaId) {
        MatriculaRemotaDTO matricula;
        try {
            matricula = obtenerMatricula(matriculaId);
        } catch (NotFoundException ex) {
            throw new BusinessException("La matrícula indicada no existe");
        }
        if (!"ACTIVA".equals(matricula.getEstado())) {
            throw new BusinessException("La matrícula " + matriculaId + " no está activa");
        }
    }

    private MatriculaRemotaDTO obtenerMatricula(Long matriculaId) {
        try {
            RemoteApiResponse<MatriculaRemotaDTO> response = matriculasClient.buscarPorId(matriculaId);
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new NotFoundException("La matrícula " + matriculaId + " no existe");
            }
            return response.getData();
        } catch (FeignException.NotFound ex) {
            throw new NotFoundException("La matrícula " + matriculaId + " no existe");
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            throw new RemoteServiceException("matriculas-service rechazó el token al consultar la matrícula");
        } catch (FeignException ex) {
            throw new RemoteServiceException("No fue posible consultar la matrícula (código remoto " + ex.status() + ")");
        }
    }
}
