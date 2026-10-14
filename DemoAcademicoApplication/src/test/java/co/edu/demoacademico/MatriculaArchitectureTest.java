package co.edu.demoacademico;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaArchitectureTest {

    @Test
    void matriculasUsaPortsYNoRepositoriosAjenos() throws IOException {
        Path modulo = Path.of("src/main/java/co/edu/demoacademico/matriculas");
        assertTrue(Files.isDirectory(modulo), "No existe el módulo de matrículas");

        try (Stream<Path> archivos = Files.walk(modulo)) {
            archivos.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                String codigo;
                try {
                    codigo = Files.readString(path);
                } catch (IOException ex) {
                    throw new IllegalStateException(ex);
                }
                assertFalse(codigo.contains("estudiantes.repository"), path.toString());
                assertFalse(codigo.contains("grupos.repository"), path.toString());
                assertFalse(codigo.contains("EstudianteRepository"), path.toString());
                assertFalse(codigo.contains("GrupoRepository"), path.toString());
            });
        }

        String servicio = Files.readString(modulo.resolve("service/MatriculaServiceImpl.java"));
        assertTrue(servicio.contains("co.edu.demoacademico.estudiantes.port.EstudianteQueryPort"));
        assertTrue(servicio.contains("co.edu.demoacademico.grupos.port.GrupoQueryPort"));
        assertTrue(servicio.contains("MatriculaRepository"));

        assertTrue(Files.exists(Path.of("src/main/java/co/edu/demoacademico/estudiantes/port/EstudianteQueryPort.java")));
        assertTrue(Files.exists(Path.of("src/main/java/co/edu/demoacademico/grupos/port/GrupoQueryPort.java")));
    }
}
