package co.edu.calificacionesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CalificacionesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalificacionesServiceApplication.class, args);
    }
}
