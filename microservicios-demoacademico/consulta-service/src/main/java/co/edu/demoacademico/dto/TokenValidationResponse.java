package co.edu.demoacademico.dto;

public class TokenValidationResponse {

    private Boolean valid;
    private String username;
    private String role;
    private String message;

    public Boolean getValid(){return valid;}
    public void setValid(Boolean valid){this.valid = valid;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getRole(){return role;}
    public void setRole(String role){this.role = role;}

    public String getMessage() {return message;}
    public void setMessage(String message){this.message = message;}
}
