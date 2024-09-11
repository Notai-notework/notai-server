package notai.domain.chat.message.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageAddRequest {
    private String message;
}
