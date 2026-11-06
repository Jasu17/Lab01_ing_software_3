package co.edu.calificacionesservice.service;

import co.edu.calificacionesservice.client.MatriculasClient;
import co.edu.calificacionesservice.dto.MatriculaRemotaDTO;
import co.edu.calificacionesservice.dto.RemoteApiResponse;
import co.edu.calificacionesservice.exception.BusinessException;
import co.edu.calificacionesservice.exception.NotFoundException;
import co.edu.calificacionesservice.model.Calificacion;
import co.edu.calificacionesservice.repository.CalificacionRepository;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceImplTest {

    private static final Request REQUEST = Request.create(
            Request.HttpMethod.GET, "/api/remoto", Map.of(), null, StandardCharsets.UTF_8, null);

    @Mock
    private CalificacionRepository repository;

    @Mock
    private MatriculasClient matriculasClient;

    @InjectMocks
    private CalificacionServiceImpl service;

    @Test
    void registraLaNotaConElUsuarioDelToken() {
        when(matriculasClient.buscarPorId(1L)).thenReturn(matricula("ACTIVA"));
        when(repository.save(any(Calificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        Calificacion calificacion = service.registrar(1L, 1, new BigDecimal("4.5"), "docente");

        assertThat(calificacion.getRegistradoPor()).isEqualTo("docente");
        assertThat(calificacion.getFechaRegistro()).isNotNull();
    }

    @Test
    void noRegistraNotasEnUnaMatriculaAnulada() {
        when(matriculasClient.buscarPorId(1L)).thenReturn(matricula("ANULADA"));

        assertThatThrownBy(() -> service.registrar(1L, 1, new BigDecimal("4.5"), "docente"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("no está activa");
    }

    @Test
    void noRegistraDosNotasDelMismoCorte() {
        when(matriculasClient.buscarPorId(1L)).thenReturn(matricula("ACTIVA"));
        when(repository.existsByMatriculaIdAndCorte(1L, 2)).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(1L, 2, new BigDecimal("3.0"), "docente"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("use PUT");
        verify(repository, never()).save(any());
    }

    @Test
    void unaMatriculaInexistenteEnElCuerpoEsErrorDeDatos() {
        when(matriculasClient.buscarPorId(99L))
                .thenThrow(new FeignException.NotFound("no existe", REQUEST, null, null));

        assertThatThrownBy(() -> service.registrar(99L, 1, new BigDecimal("3.0"), "docente"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void elResumenDeUnaMatriculaInexistenteEs404() {
        when(matriculasClient.buscarPorId(99L))
                .thenThrow(new FeignException.NotFound("no existe", REQUEST, null, null));

        assertThatThrownBy(() -> service.resumen(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void promedioParcialSeCalculaSobreLosCortesRegistrados() {
        // (4.0 × 0.3 + 3.0 × 0.3) / 0.6 = 3.50
        assertThat(CalificacionServiceImpl.calcularPromedio(List.of(nota(1, "4.0"), nota(2, "3.0"))))
                .isEqualByComparingTo("3.50");
    }

    @Test
    void promedioDefinitivoConLosTresCortes() {
        // 4.0 × 0.3 + 3.0 × 0.3 + 5.0 × 0.4 = 4.10
        assertThat(CalificacionServiceImpl.calcularPromedio(List.of(nota(1, "4.0"), nota(2, "3.0"), nota(3, "5.0"))))
                .isEqualByComparingTo("4.10");
    }

    @Test
    void sinNotasNoHayPromedio() {
        assertThat(CalificacionServiceImpl.calcularPromedio(List.of())).isNull();
    }

    private static RemoteApiResponse<MatriculaRemotaDTO> matricula(String estado) {
        MatriculaRemotaDTO m = new MatriculaRemotaDTO();
        m.setId(1L);
        m.setEstado(estado);
        RemoteApiResponse<MatriculaRemotaDTO> response = new RemoteApiResponse<>();
        response.setSuccess(true);
        response.setData(m);
        return response;
    }

    private static Calificacion nota(int corte, String valor) {
        Calificacion c = new Calificacion();
        c.setCorte(corte);
        c.setNota(new BigDecimal(valor));
        return c;
    }
}
