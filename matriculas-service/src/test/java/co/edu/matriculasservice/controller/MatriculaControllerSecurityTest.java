package co.edu.matriculasservice.controller;

import co.edu.matriculasservice.client.AuthClient;
import co.edu.matriculasservice.config.SecurityConfig;
import co.edu.matriculasservice.dto.TokenValidationResponse;
import co.edu.matriculasservice.handler.MatriculaHandler;
import co.edu.matriculasservice.security.CustomAccessDeniedHandler;
import co.edu.matriculasservice.security.CustomAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatriculaController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class MatriculaControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthClient authClient;

    @MockBean
    private MatriculaHandler handler;

    @Test
    void elEstudianteConsultaSoloSusMatriculas() throws Exception {
        token("tk-estudiante", "ESTUDIANTE", 1L);

        mockMvc.perform(get("/api/matriculas/mias").header("Authorization", "Bearer tk-estudiante"))
                .andExpect(status().isOk());
        verify(handler).listarPorEstudiante(1L);
    }

    @Test
    void elEstudianteNoPuedeListarTodasLasMatriculas() throws Exception {
        token("tk-estudiante", "ESTUDIANTE", 1L);

        mockMvc.perform(get("/api/matriculas").header("Authorization", "Bearer tk-estudiante"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unAdminNoTieneMisMatriculas() throws Exception {
        token("tk-admin", "ADMIN", null);

        mockMvc.perform(get("/api/matriculas/mias").header("Authorization", "Bearer tk-admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unEstudianteSinEstudianteAsociadoRecibe403() throws Exception {
        token("tk-huerfano", "ESTUDIANTE", null);

        mockMvc.perform(get("/api/matriculas/mias").header("Authorization", "Bearer tk-huerfano"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("AUTH_FORBIDDEN"));
    }

    private void token(String token, String rol, Long estudianteId) {
        TokenValidationResponse validation = new TokenValidationResponse();
        validation.setValid(true);
        validation.setUsername("usuario-" + rol.toLowerCase());
        validation.setRole(rol);
        validation.setEstudianteId(estudianteId);
        when(authClient.validate(eq("Bearer " + token))).thenReturn(validation);
    }
}
