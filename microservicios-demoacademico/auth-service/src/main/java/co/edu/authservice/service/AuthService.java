package co.edu.authservice.service;

import co.edu.authservice.model.AuthUser;
import co.edu.authservice.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService{

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthUserRepository authUserRepository,
                       PasswordEncoder passwordEncoder){
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password){
        return authUserRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .orElse(false);
    }

    public AuthUser findByUsername(String username){
        return authUserRepository.findByUsername(username).orElse(null);
    }
}