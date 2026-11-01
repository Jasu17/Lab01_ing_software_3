package co.edu.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "auth_user", uniqueConstraints = {
        @UniqueConstraint(name = "uk_auth_user_username", columnNames =
        {"username"})
})

public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String username;

    @Column(nullable = false, length = 225)
    private String passwordHash;

    @Column(nullable = false, length = 49)
    private String role;

    public AuthUser(){}

    public AuthUser(String username, String passwordHash, String role){
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId(){return id;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String passwordHash){this.passwordHash = passwordHash;}

    public String getRole(){return role;}
    public void setRole(String role){this.role = role;}
}