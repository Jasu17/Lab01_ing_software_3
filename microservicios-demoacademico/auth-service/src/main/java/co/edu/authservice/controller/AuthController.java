package co.edu.authservice.controller;

import co.edu.authservice.dto.LoginRequest;
import co.edu.authservice.dto.LoginResponse;
import co.edu.authservice.model.AuthUser;
import co.edu.authservice.security.JwtUtil;
import co.edu.authservice.security.TokenUtils;
import co.edu.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        if (authService.authenticate(request.getUsername(), request.getPassword())) {
            AuthUser user = authService.findByUsername(request.getUsername());
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales inválidas");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token) {
        String normalizedToken = TokenUtils.normalize(token);

        Map<String, Object> response = new HashMap<>();

        if (normalizedToken == null) {
            response.put("valid", false);
            response.put("message", "Token no enviado o formato inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        boolean valid = jwtUtil.validateToken(normalizedToken);
        response.put("valid", valid);

        if (valid) {
            response.put("username", jwtUtil.getUsername(normalizedToken));
            response.put("role", jwtUtil.getRole(normalizedToken));
            return ResponseEntity.ok(response);
        }

        response.put("message", "Token inválido o expirado");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}