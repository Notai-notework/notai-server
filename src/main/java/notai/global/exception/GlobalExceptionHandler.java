package notai.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException customException) {
        ErrorResponse response = customException.toResponse();

        log.error("### 예외 발생 ###  status: {}, message: {}", response.getStatus(),
            response.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
