package co.edu.demoacademico.client;

import co.edu.demoacademico.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authClient", url = "${services.auth.url}")
public interface AuthFeignClient {

    @GetMapping("/auth/validate")
    TokenValidationResponse validate(@RequestParam("token") String token);
}
