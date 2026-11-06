package co.edu.cursosservice.config;

import co.edu.cursosservice.model.Curso;
import co.edu.cursosservice.repository.CursoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedCursos(CursoRepository repository) {
        return args -> {
            if (repository.count() > 0) return;

            repository.save(curso("IS3-001", "Ingeniería de Software III", "Arquitectura y microservicios", 4, "Docente principal", 30));
            repository.save(curso("BD2-001", "Bases de Datos II", "Modelado avanzado y SQL", 3, "Docente de bases de datos", 25));
            repository.save(curso("RED-001", "Redes de Computadores", "Fundamentos de redes TCP/IP", 3, "Docente de redes", 2));
        };
    }

    private Curso curso(String codigo, String nombre, String descripcion, int creditos, String docente, int cupo) {
        Curso c = new Curso();
        c.setCodigo(codigo);
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setCreditos(creditos);
        c.setDocenteResponsable(docente);
        c.setCupoMaximo(cupo);
        return c;
    }
}
