package notai.domain.file.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    DOCUMENT("텍스트 문서"),
    IMAGE("이미지");

    private final String detail;
}
