package co.edu.estudiantesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EstudiantesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EstudiantesServiceApplication.class, args);
    }
}
