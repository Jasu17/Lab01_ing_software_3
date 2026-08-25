package co.edu.demoacademico.model;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String mensaje;
    private int codigo;
    private String detalle;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensaje, int codigo, String detalle) {
        this.mensaje = mensaje;
        this.codigo = codigo;
        this.detalle = detalle;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}