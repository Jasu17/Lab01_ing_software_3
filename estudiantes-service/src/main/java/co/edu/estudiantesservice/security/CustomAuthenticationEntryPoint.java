package co.edu.estudiantesservice.security;

import co.edu.estudiantesservice.api.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String errorCode = "AUTH_UNAUTHORIZED";
        String message = "Debe autenticarse para acceder a este recurso";

        if (authException instanceof CustomAuthenticationException customEx) {
            errorCode = customEx.getErrorCode();
            message = customEx.getMessage();
        }

        ApiErrorResponse body = ApiErrorResponse.of(
                message,
                errorCode,
                HttpServletResponse.SC_UNAUTHORIZED,
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}
