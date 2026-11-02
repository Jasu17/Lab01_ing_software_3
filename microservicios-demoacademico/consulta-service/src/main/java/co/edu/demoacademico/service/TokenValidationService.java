package co.edu.demoacademico.service;

import co.edu.demoacademico.client.AuthFeignClient;
import co.edu.demoacademico.dto.TokenValidationResponse;
import co.edu.demoacademico.security.TokenUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    private final AuthFeignClient authFeignClient;

    public TokenValidationService(AuthFeignClient authFeignClient){
        this.authFeignClient = authFeignClient;
    }

    public boolean isValid(String rawToken){
        try {

            String token = TokenUtils.normalize(rawToken);

            if (token == null) {
                return false;
            }

            TokenValidationResponse response = authFeignClient.validate(token);
            return response != null && Boolean.TRUE.equals(response.getValid());
        } catch (Exception e){
            return false;
        }
    }
}
