package co.edu.calificacionesservice.config;

import co.edu.calificacionesservice.security.AuthValidationFilter;
import co.edu.calificacionesservice.security.CustomAccessDeniedHandler;
import co.edu.calificacionesservice.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthValidationFilter authValidationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(AuthValidationFilter authValidationFilter,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.authValidationFilter = authValidationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/h2-console/**", "/error").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/calificaciones/mias").hasAuthority("ESTUDIANTE")
                .requestMatchers(HttpMethod.GET, "/api/calificaciones/**").hasAnyAuthority("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/calificaciones/**").hasAnyAuthority("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.PUT, "/api/calificaciones/**").hasAnyAuthority("ADMIN", "DOCENTE")

                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .addFilterBefore(authValidationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
