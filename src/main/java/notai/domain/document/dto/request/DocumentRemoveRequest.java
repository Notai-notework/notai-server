package notai.domain.document.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentRemoveRequest {

    @NotNull(message = "문서 id를 입력해주세요")
    private Long documentId;
}
