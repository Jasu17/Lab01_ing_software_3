package co.edu.matriculasservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignAuthPropagationConfig {

    @Bean
    public RequestInterceptor authTokenPropagationInterceptor() {
        return requestTemplate -> {
            if (requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION)) {
                return;
            }
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes instanceof ServletRequestAttributes servletAttributes) {
                HttpServletRequest request = servletAttributes.getRequest();
                String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null && !authorization.isBlank()) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, authorization);
                }
            }
        };
    }
}
