package co.edu.authservice.service;

import co.edu.authservice.dto.LoginRequest;
import co.edu.authservice.dto.LoginResponse;
import co.edu.authservice.dto.TokenValidationResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    TokenValidationResponse validate(String authorization);
}
