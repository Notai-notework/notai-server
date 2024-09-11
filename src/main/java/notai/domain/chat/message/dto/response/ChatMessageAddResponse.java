package notai.domain.chat.message.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageAddResponse {
    private Long sendMessageId;
    private String sendMessage;
    private Long aiMessageId;
    private String aiMessage;
}
