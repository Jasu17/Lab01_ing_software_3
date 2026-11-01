package co.edu.authservice.config;

import co.edu.authservice.model.AuthUser;
import co.edu.authservice.repository.AuthUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthDataSeeder {

    @Bean
    CommandLineRunner seedUsers(AuthUserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            repository.save(new AuthUser(
                    "admin",
                    passwordEncoder.encode("Admin2026*"),
                    "ADMIN"
            ));

            repository.save(new AuthUser(
                    "docente",
                    passwordEncoder.encode("Docente2026*"),
                    "DOCENTE"
            ));

            repository.save(new AuthUser(
                    "estudiante1",
                    passwordEncoder.encode("Estu2026*"),
                    "ESTUDIANTE"
            ));
        };
    }
}