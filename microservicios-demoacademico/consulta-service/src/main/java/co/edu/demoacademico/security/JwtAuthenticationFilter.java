package co.edu.demoacademico.security;

import co.edu.demoacademico.service.TokenValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final TokenValidationService tokenValidationService;

    public JwtAuthenticationFilter(TokenValidationService tokenValidationService){
        this.tokenValidationService = tokenValidationService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        String method = request.getMethod();

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/h2-console")
                || ("POST".equalsIgnoreCase(method) && "/api/estudiantes".equals(path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/estudiantes")) {
            String authHeader = request.getHeader("Authorization");
            String token = TokenUtils.normalize(authHeader);

            if (token == null) {
                unauthorized(response, "Token no enviado o formato inválido");
                return;
            }

            if (!tokenValidationService.isValid(token)) {
                unauthorized(response, "Token inválido o expirado");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(message);
    }
}
