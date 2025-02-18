package notai.domain.chat.message.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatMessageType {

    USER("유저 메시지"),
    AI("AI 메시지");

    private final String detail;
}
