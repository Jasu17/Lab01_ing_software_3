package co.edu.estudiantesservice.service;

import co.edu.estudiantesservice.client.MatriculasClient;
import co.edu.estudiantesservice.dto.RemoteApiResponse;
import co.edu.estudiantesservice.exception.ConflictException;
import co.edu.estudiantesservice.exception.RemoteServiceException;
import co.edu.estudiantesservice.model.Estudiante;
import co.edu.estudiantesservice.repository.EstudianteRepository;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceImplTest {

    private static final Request REQUEST = Request.create(
            Request.HttpMethod.GET, "/api/remoto", Map.of(), null, StandardCharsets.UTF_8, null);

    @Mock
    private EstudianteRepository repository;

    @Mock
    private MatriculasClient matriculasClient;

    @InjectMocks
    private EstudianteServiceImpl service;

    @Test
    void noEliminaUnEstudianteConMatriculasActivas() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Estudiante()));
        when(matriculasClient.contarActivasPorEstudiante(1L)).thenReturn(conteo(2L));

        assertThatThrownBy(() -> service.eliminar(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("2 matrícula(s) activa(s)");
        verify(repository, never()).delete(any());
    }

    @Test
    void eliminaUnEstudianteSinMatriculasActivas() {
        Estudiante estudiante = new Estudiante();
        when(repository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(matriculasClient.contarActivasPorEstudiante(1L)).thenReturn(conteo(0L));

        service.eliminar(1L);

        verify(repository).delete(estudiante);
    }

    @Test
    void noEliminaSiNoSePuedeVerificarConMatriculas() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Estudiante()));
        when(matriculasClient.contarActivasPorEstudiante(1L))
                .thenThrow(new FeignException.ServiceUnavailable("caído", REQUEST, null, null));

        assertThatThrownBy(() -> service.eliminar(1L))
                .isInstanceOf(RemoteServiceException.class);
        verify(repository, never()).delete(any());
    }

    private static RemoteApiResponse<Long> conteo(long n) {
        RemoteApiResponse<Long> response = new RemoteApiResponse<>();
        response.setSuccess(true);
        response.setData(n);
        return response;
    }
}
