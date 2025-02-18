package notai.domain.chat.message.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageAddResponse {
    private Long sendMessageId;
    private String sendMessage;
    private LocalDateTime sendMessageCreatedAt;
    private Long aiMessageId;
    private String aiMessage;
    private LocalDateTime aiMessageCreatedAt;
}
