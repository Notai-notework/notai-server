package notai.domain.chat.room.service;

import notai.domain.chat.room.dto.ChatRoomIdResponse;
import notai.domain.user.entity.User;

public interface ChatRoomService {

    ChatRoomIdResponse addChatRoom(User user);

    void removeChatRoom(Long chatRoomId, User user);
}
