package notai.domain.chat.message.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageAddRequest {

    @NotBlank(message = "메시지를 입력해주세요")
    private String content;
}
