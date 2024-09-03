package notai.global.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException ex) {

        ErrorResponse response = ex.toResponse();

        log.error("### 예외 발생 ###  status: {}, message: {}", response.getStatus(),
            response.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(
        MethodArgumentNotValidException ex) {

        ErrorResponse response = ErrorResponse.builder()
            .status(400)
            .errorCode("VALIDATION_ERROR")
            .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()).build();

        return ResponseEntity.status(404).body(response);
    }
}
