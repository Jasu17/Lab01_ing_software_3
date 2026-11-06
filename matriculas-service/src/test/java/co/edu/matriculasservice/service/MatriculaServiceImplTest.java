package co.edu.matriculasservice.service;

import co.edu.matriculasservice.client.CursosClient;
import co.edu.matriculasservice.client.EstudiantesClient;
import co.edu.matriculasservice.dto.CursoRemotoDTO;
import co.edu.matriculasservice.dto.EstudianteRemotoDTO;
import co.edu.matriculasservice.dto.RemoteApiResponse;
import co.edu.matriculasservice.exception.BusinessException;
import co.edu.matriculasservice.model.EstadoMatricula;
import co.edu.matriculasservice.model.Matricula;
import co.edu.matriculasservice.repository.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceImplTest {

    @Mock
    private MatriculaRepository repository;

    @Mock
    private EstudiantesClient estudiantesClient;

    @Mock
    private CursosClient cursosClient;

    @InjectMocks
    private MatriculaServiceImpl service;

    @Test
    void rechazaLaMatriculaCuandoElCursoEstaLleno() {
        prepararRemotos(2);
        when(repository.countByCursoIdAndEstado(3L, EstadoMatricula.ACTIVA)).thenReturn(2L);

        assertThatThrownBy(() -> service.registrar(1L, 3L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("no tiene cupos disponibles");
        verify(repository, never()).save(any());
    }

    @Test
    void registraLaMatriculaCuandoQuedaCupo() {
        prepararRemotos(2);
        when(repository.countByCursoIdAndEstado(3L, EstadoMatricula.ACTIVA)).thenReturn(1L);
        when(repository.save(any(Matricula.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.registrar(1L, 3L).getEstado()).isEqualTo(EstadoMatricula.ACTIVA);
    }

    @Test
    void elConteoExigeExactamenteUnFiltro() {
        assertThatThrownBy(() -> service.contarActivas(null, null)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> service.contarActivas(1L, 3L)).isInstanceOf(BusinessException.class);
    }

    @Test
    void cuentaLasMatriculasActivasDeUnEstudiante() {
        when(repository.countByEstudianteIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(4L);

        assertThat(service.contarActivas(1L, null)).isEqualTo(4L);
    }

    private void prepararRemotos(int cupo) {
        CursoRemotoDTO curso = new CursoRemotoDTO();
        curso.setCupoMaximo(cupo);
        when(estudiantesClient.buscarPorId(1L)).thenReturn(ok(new EstudianteRemotoDTO()));
        when(cursosClient.buscarPorId(3L)).thenReturn(ok(curso));
    }

    private static <T> RemoteApiResponse<T> ok(T data) {
        RemoteApiResponse<T> response = new RemoteApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
}
