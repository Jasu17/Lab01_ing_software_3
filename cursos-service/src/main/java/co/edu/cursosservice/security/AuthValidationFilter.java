package co.edu.cursosservice.security;

import co.edu.cursosservice.client.AuthClient;
import co.edu.cursosservice.dto.TokenValidationResponse;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthValidationFilter extends OncePerRequestFilter {

    private final AuthClient authClient;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public AuthValidationFilter(AuthClient authClient,
                                CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.authClient = authClient;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            reject(request, response, "Debe enviar un token Bearer válido", "AUTH_HEADER_MISSING");
            return;
        }

        TokenValidationResponse validation;
        try {
            validation = authClient.validate(authHeader);
        } catch (FeignException.Unauthorized ex) {
            reject(request, response, "El token no es válido o expiró", "TOKEN_INVALID");
            return;
        } catch (FeignException ex) {
            reject(request, response, "No fue posible validar el token con auth-service", "TOKEN_VALIDATION_ERROR");
            return;
        }

        if (validation == null || !validation.isValid() || validation.getRole() == null) {
            reject(request, response, "El token no es válido o no contiene rol", "TOKEN_INVALID");
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                validation.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(validation.getRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void reject(HttpServletRequest request, HttpServletResponse response,
                        String message, String errorCode) throws IOException {
        SecurityContextHolder.clearContext();
        authenticationEntryPoint.commence(request, response, new CustomAuthenticationException(message, errorCode));
    }
}
