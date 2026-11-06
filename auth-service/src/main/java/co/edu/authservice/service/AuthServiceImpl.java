package co.edu.authservice.service;

import co.edu.authservice.dto.LoginRequest;
import co.edu.authservice.dto.LoginResponse;
import co.edu.authservice.dto.TokenValidationResponse;
import co.edu.authservice.exception.InvalidCredentialsException;
import co.edu.authservice.exception.InvalidTokenException;
import co.edu.authservice.model.Usuario;
import co.edu.authservice.repository.UsuarioRepository;
import co.edu.authservice.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = jwtUtils.generateToken(usuario.getUsername(), usuario.getRole(), usuario.getEstudianteId());
        return new LoginResponse(token, usuario.getUsername(), usuario.getRole());
    }

    @Override
    public TokenValidationResponse validate(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new InvalidTokenException("Debe enviar un token Bearer válido");
        }

        try {
            String token = authorization.replace("Bearer ", "");
            var claims = jwtUtils.parseToken(token);
            return new TokenValidationResponse(
                    true,
                    claims.getSubject(),
                    claims.get("role", String.class),
                    claims.get("estudianteId", Long.class)
            );
        } catch (Exception ex) {
            throw new InvalidTokenException("Token inválido o expirado");
        }
    }
}