package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DocumentErrorCode implements ErrorCode {

    DOCUMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 문서가 존재하지 않습니다"),
    DOCUMENT_NOT_POSSESSION(HttpStatus.BAD_REQUEST, "해당 문서의 등록자가 아닙니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
