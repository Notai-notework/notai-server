package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다"),
    USER_PASSWORD_NOT_CORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
