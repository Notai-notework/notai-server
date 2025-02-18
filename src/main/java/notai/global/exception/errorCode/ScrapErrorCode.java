package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScrapErrorCode implements ErrorCode {

    SCRAP_NOT_FOUND(HttpStatus.BAD_REQUEST, "스크랩 정보가 존재하지 않습니다"),
    SCRAP_NOT_POSSESSION(HttpStatus.BAD_REQUEST, "해당 스크랩은 본인에게 속하지 않습니다"),
    SCRAP_ALREADY(HttpStatus.CONFLICT, "이미 스크랩한 문서입니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
