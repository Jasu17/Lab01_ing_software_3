package co.edu.estudiantesservice.config;

import co.edu.estudiantesservice.security.AuthValidationFilter;
import co.edu.estudiantesservice.security.CustomAccessDeniedHandler;
import co.edu.estudiantesservice.security.CustomAuthenticationEntryPoint;
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

                .requestMatchers(HttpMethod.GET, "/api/estudiantes/**").hasAnyAuthority("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/estudiantes/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/estudiantes/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/estudiantes/**").hasAuthority("ADMIN")

                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .addFilterBefore(authValidationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
