package notai.domain.chat.message.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import notai.domain.user.dto.response.UserSummaryResponse;

@Data
@Builder
public class ChatMessageDetailResponse {
    private Long id;
    private UserSummaryResponse user;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
