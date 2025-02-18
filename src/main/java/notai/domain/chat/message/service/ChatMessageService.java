package notai.domain.chat.message.service;

import java.util.List;
import notai.domain.chat.message.dto.request.ChatMessageAddRequest;
import notai.domain.chat.message.dto.response.ChatMessageAddResponse;
import notai.domain.chat.message.dto.response.ChatMessageDetailResponse;
import notai.domain.user.entity.User;

public interface ChatMessageService {

    List<ChatMessageDetailResponse> findAllChatMessage(User user);

    ChatMessageAddResponse addChatMessage(ChatMessageAddRequest request, User user);
}
