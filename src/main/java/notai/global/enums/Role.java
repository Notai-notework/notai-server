package notai.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("관리자"),
    AI("챗봇"),
    USER("유저");

    private final String detail;
}
