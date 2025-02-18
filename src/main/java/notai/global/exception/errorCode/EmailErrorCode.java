package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {

    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다"),
    EMAIL_CODE_SENDING_ERROR(HttpStatus.BAD_GATEWAY, "인증번호를 요청할 수 없습니다"),
    EMAIL_CODE_VALID_ERROR(HttpStatus.BAD_REQUEST,"이메일 인증번호가 일치하지 않습니다"),
    EMAIL_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"이메일 인증번호 유효기간이 만료되었습니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
