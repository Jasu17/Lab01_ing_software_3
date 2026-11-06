package co.edu.calificacionesservice.client;

import co.edu.calificacionesservice.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authClient", url = "${auth-service.url}")
public interface AuthClient {

    @GetMapping("/auth/validate")
    TokenValidationResponse validate(@RequestHeader("Authorization") String authorization);
}
