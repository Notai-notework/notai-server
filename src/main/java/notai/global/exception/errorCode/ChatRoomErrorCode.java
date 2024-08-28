package notai.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방이 존재하지 않습니다"),
    CHAT_ROOM_NOT_BELONG(HttpStatus.FORBIDDEN, "채팅방 멤버가 아닙니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
