package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    ACCESS_TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다"),
    ACCESS_TOKEN_MISSING(HttpStatus.FORBIDDEN, "요청에 액세스 토큰이 존재하지 않습니다"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다"),

    REFRESH_TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다"),
    REFRESH_TOKEN_MISSING(HttpStatus.FORBIDDEN, "요청에 리프레시 토큰이 존재하지 않습니다"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
