package co.edu.matriculasservice.security;

import java.security.Principal;

public record UsuarioAutenticado(String username, String role, Long estudianteId) implements Principal {

    @Override
    public String getName() {
        return username;
    }
}
