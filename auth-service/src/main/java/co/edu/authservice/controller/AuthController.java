package co.edu.authservice.controller;

import co.edu.authservice.dto.LoginRequest;
import co.edu.authservice.dto.LoginResponse;
import co.edu.authservice.dto.TokenValidationResponse;
import co.edu.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validate")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TokenValidationResponse> validate(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(authService.validate(authorization));
    }
}