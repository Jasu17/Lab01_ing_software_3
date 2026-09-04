package co.edu.demoacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder(){}

    public static <T> ResponseEntity<ApiResponse<T>> ok (String message, T data){
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, message, data));
    }

    public static ResponseEntity<ApiResponse<Object>> badRequest(String message) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, message, null));
    }

    public static ResponseEntity<ApiResponse<Object>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, message, null));
    }

    public static ResponseEntity<ApiResponse<Object>> internalError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, message, null));
    }
}
