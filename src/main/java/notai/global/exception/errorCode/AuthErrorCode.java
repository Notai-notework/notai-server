package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    AUTH_EMAIL_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다"),
    AUTH_PASSWORD_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 비밀번호입니다"),
    AUTH_NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 별명입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
