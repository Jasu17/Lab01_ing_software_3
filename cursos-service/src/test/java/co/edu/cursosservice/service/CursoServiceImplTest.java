package co.edu.cursosservice.service;

import co.edu.cursosservice.client.MatriculasClient;
import co.edu.cursosservice.dto.RemoteApiResponse;
import co.edu.cursosservice.exception.BusinessException;
import co.edu.cursosservice.exception.ConflictException;
import co.edu.cursosservice.model.Curso;
import co.edu.cursosservice.repository.CursoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoServiceImplTest {

    @Mock
    private CursoRepository repository;

    @Mock
    private MatriculasClient matriculasClient;

    @InjectMocks
    private CursoServiceImpl service;

    @Test
    void noEliminaUnCursoConMatriculasActivas() {
        when(repository.findById(3L)).thenReturn(Optional.of(curso(2)));
        when(matriculasClient.contarActivasPorCurso(3L)).thenReturn(conteo(2L));

        assertThatThrownBy(() -> service.eliminar(3L))
                .isInstanceOf(ConflictException.class);
        verify(repository, never()).delete(any());
    }

    @Test
    void noReduceElCupoPorDebajoDeLasMatriculasActivas() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso(30)));
        when(matriculasClient.contarActivasPorCurso(1L)).thenReturn(conteo(8L));

        assertThatThrownBy(() -> service.actualizar(1L, curso(5)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("8 matrícula(s)");
    }

    @Test
    void reduceElCupoSiAlcanzaParaLasMatriculasActivas() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso(30)));
        when(matriculasClient.contarActivasPorCurso(1L)).thenReturn(conteo(8L));
        when(repository.save(any(Curso.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.actualizar(1L, curso(10)).getCupoMaximo()).isEqualTo(10);
    }

    @Test
    void aumentarElCupoNoConsultaMatriculas() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso(2)));
        when(repository.save(any(Curso.class))).thenAnswer(inv -> inv.getArgument(0));

        service.actualizar(1L, curso(5));

        verifyNoInteractions(matriculasClient);
    }

    private static Curso curso(int cupo) {
        Curso c = new Curso();
        c.setCodigo("RED-001");
        c.setNombre("Redes");
        c.setCreditos(3);
        c.setDocenteResponsable("Docente");
        c.setCupoMaximo(cupo);
        return c;
    }

    private static RemoteApiResponse<Long> conteo(long n) {
        RemoteApiResponse<Long> response = new RemoteApiResponse<>();
        response.setSuccess(true);
        response.setData(n);
        return response;
    }
}
