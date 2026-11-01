package co.edu.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "El Username es obligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
}