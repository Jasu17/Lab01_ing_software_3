package co.edu.matriculasservice.api;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(boolean success, String message, String errorCode, int status, String path, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
    }

    public static ApiErrorResponse of(String message, String errorCode, int status, String path) {
        return new ApiErrorResponse(false, message, errorCode, status, path, LocalDateTime.now());
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
