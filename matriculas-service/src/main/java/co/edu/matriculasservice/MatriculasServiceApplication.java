package co.edu.matriculasservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MatriculasServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatriculasServiceApplication.class, args);
    }
}
