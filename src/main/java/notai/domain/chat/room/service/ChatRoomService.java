package notai.domain.chat.room.service;

import lombok.RequiredArgsConstructor;
import notai.domain.chat.room.dto.ChatRoomIdResponse;
import notai.domain.chat.room.entity.ChatRoom;
import notai.domain.chat.room.repository.ChatRoomRepository;
import notai.domain.user.entity.User;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.ChatRoomErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomIdResponse addChatRoom(User user) {

        ChatRoom chatRoom = chatRoomRepository.findByUserId(user.getId()).orElse(ChatRoom.builder()
            .user(user).build());

        ChatRoom saveedChatRoom = chatRoomRepository.save(chatRoom);

        return saveedChatRoom.toDTO();
    }

    public void removeChatRoom(Long chatRoomId, User user) {

        ChatRoom foundChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
            new CustomException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        // 채팅방 멤버가 아니면
        if (!foundChatRoom.isMember(user.getId())) {
            throw new CustomException(ChatRoomErrorCode.CHAT_ROOM_NOT_BELONG);
        }

        chatRoomRepository.delete(foundChatRoom);
    }
}
