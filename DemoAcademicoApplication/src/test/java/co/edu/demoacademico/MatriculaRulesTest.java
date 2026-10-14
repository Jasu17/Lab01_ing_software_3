package co.edu.demoacademico;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "app.seed.enabled=false")
@AutoConfigureMockMvc
class MatriculaRulesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void matriculaExigeExistenciaDuplicadoYCupo() throws Exception {
        long programaId = idDe(mockMvc.perform(post("/api/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"PROG-LAB03","nombre":"Ingeniería de Software"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Programa creado"))
                .andReturn());

        long asignaturaId = idDe(mockMvc.perform(post("/api/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"ASIG-IS3","nombre":"Ingeniería de Software III","creditos":3,"programaId":%d}
                                """.formatted(programaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn());

        long grupoId = idDe(mockMvc.perform(post("/api/grupos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigoGrupo":"G-IS3-01","cupoMax":2,"asignaturaId":%d}
                                """.formatted(asignaturaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.cupoMax").value(2))
                .andReturn());

        long estudiante1 = crearEstudiante("Ana Uno", "ana.uno.lab03@test.edu");
        long estudiante2 = crearEstudiante("Luis Dos", "luis.dos.lab03@test.edu");
        long estudiante3 = crearEstudiante("Mia Tres", "mia.tres.lab03@test.edu");

        matricular(estudiante1, grupoId)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Matrícula registrada"))
                .andExpect(jsonPath("$.data.estudianteId").value(estudiante1))
                .andExpect(jsonPath("$.data.grupoId").value(grupoId));

        matricular(estudiante1, grupoId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El estudiante ya está matriculado en ese grupo"));

        matricular(estudiante2, grupoId)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.estudianteId").value(estudiante2));

        matricular(estudiante3, grupoId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El grupo no tiene cupo disponible"));

        matricular(999999L, grupoId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Estudiante no encontrado: 999999"));

        matricular(estudiante3, 999999L)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Grupo no encontrado: 999999"));
    }

    @Test
    void rechazaMatriculaSinIdentificadores() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validación fallida"));
    }

    private org.springframework.test.web.servlet.ResultActions matricular(long estudianteId, long grupoId) throws Exception {
        return mockMvc.perform(post("/api/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"estudianteId":%d,"grupoId":%d}
                        """.formatted(estudianteId, grupoId)));
    }

    private long crearEstudiante(String nombre, String email) throws Exception {
        return idDe(mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"%s","email":"%s"}
                                """.formatted(nombre, email)))
                .andExpect(status().isCreated())
                .andReturn());
    }

    private long idDe(MvcResult result) throws Exception {
        JsonNode data = objectMapper.readTree(result.getResponse().getContentAsString()).path("data");
        return data.path("id").asLong();
    }
}
