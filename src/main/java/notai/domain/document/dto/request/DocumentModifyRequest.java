package notai.domain.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentModifyRequest {

    @NotNull(message = "문서 id를 입력해주세요")
    private Long documentId;

    @NotBlank(message = "문서 제목은 필수 입력 항목입니다")
    private String title;

    @NotBlank(message = "문서 내용(간단 소개)은 필수 입력 항목입니다")
    private String content;

    @NotBlank(message = "태그 이름은 필수 입력 항목입니다")
    private String tagName;
}
