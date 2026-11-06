package co.edu.estudiantesservice.config;

import co.edu.estudiantesservice.model.Estudiante;
import co.edu.estudiantesservice.repository.EstudianteRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class DataSeeder {

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.cantidad:50}")
    private int cantidad;

    @Bean
    CommandLineRunner seedData(EstudianteRepository repository) {
        return args -> {
            if (!enabled) return;
            if (repository.count() > 0) return;

            Faker faker = new Faker(new Locale("es"));

            for (int i = 0; i < cantidad; i++) {
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(faker.name().firstName());
                estudiante.setApellido(faker.name().lastName());
                estudiante.setEmail("estudiante" + i + "@demoacademico.edu");
                estudiante.setEdad(18 + (i % 20));
                repository.save(estudiante);
            }
        };
    }
}
