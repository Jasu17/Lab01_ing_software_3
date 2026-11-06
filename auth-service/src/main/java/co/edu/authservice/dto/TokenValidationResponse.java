package co.edu.authservice.dto;

public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private String role;
    private Long estudianteId;

    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String username, String role, Long estudianteId) {
        this.valid = valid;
        this.username = username;
        this.role = role;
        this.estudianteId = estudianteId;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
}
