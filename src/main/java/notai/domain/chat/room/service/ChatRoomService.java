package notai.domain.chat.room.service;

import lombok.RequiredArgsConstructor;
import notai.domain.chat.room.dto.ChatRoomRequestDTO;
import notai.domain.chat.room.dto.ChatRoomResponseDTO;
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

    public ChatRoomResponseDTO addChatRoom(User user) {

        // 새 채팅방 엔티티 생성
        ChatRoom newChatRoom = ChatRoom.builder()
            .user(user).build();

        ChatRoom saveedChatRoom = chatRoomRepository.save(newChatRoom);

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
