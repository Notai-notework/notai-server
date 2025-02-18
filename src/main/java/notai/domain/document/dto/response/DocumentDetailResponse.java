package notai.domain.document.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import notai.domain.user.dto.response.UserSummaryResponse;

@Data
@Builder
public class DocumentDetailResponse {
    private Long id;
    private String title;
    private UserSummaryResponse user;
    private String content;
    private String documentFileUrl;
    private String previewImageUrl;
    private String tagName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
