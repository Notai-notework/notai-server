package notai.domain.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentAddRequest {

    @NotBlank(message = "문서 제목은 필수 입력 항목입니다")
    private String title;

    @NotBlank(message = "문서 내용(간단 소개)은 필수 입력 항목입니다")
    private String content;

    @NotBlank(message = "태그 이름은 필수 입력 항목입니다")
    private String tagName;

    @NotNull(message = "문서 파일은 필수 항목입니다")
    private MultipartFile documentFile;
}
