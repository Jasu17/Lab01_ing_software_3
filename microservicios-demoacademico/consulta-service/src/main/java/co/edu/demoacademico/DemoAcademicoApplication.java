package co.edu.demoacademico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DemoAcademicoApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoAcademicoApplication.class, args);
    }
}