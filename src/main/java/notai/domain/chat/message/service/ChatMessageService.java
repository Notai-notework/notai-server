package notai.domain.chat.message.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import notai.domain.chat.message.dto.request.ChatMessageAddRequest;
import notai.domain.chat.message.dto.response.ChatMessageAddResponse;
import notai.domain.chat.message.dto.response.ChatMessageDetailResponse;
import notai.domain.chat.message.entity.ChatMessage;
import notai.domain.chat.message.enums.ChatMessageType;
import notai.domain.chat.message.mapper.ChatMessageMapper;
import notai.domain.chat.message.repository.ChatMessageRepository;
import notai.domain.gemini.service.GeminiService;
import notai.domain.user.entity.User;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.ChatRoomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final GeminiService geminiService;

    // 채팅 메시지 목록
    public List<ChatMessageDetailResponse> findAllChatMessage(Long roomId, User user) {

        // roomId 에러 체크
        if (!Objects.equals(roomId, user.getChatRoom().getId())) {
            throw new CustomException(ChatRoomErrorCode.CHAT_ROOM_ID_ERROR);
        }

        List<ChatMessage> foundChatMessageList = chatMessageRepository.findByChatRoomId(
            user.getChatRoom().getId());

        return foundChatMessageList.stream()
            .map(ChatMessageMapper.INSTANCE::toDetailDTO).toList();
    }

    // 메시지 저장 및 AI 답변 반환
    public ChatMessageAddResponse addChatMessage(ChatMessageAddRequest request, User user) {

        ChatMessage newChatMessage = ChatMessage.builder()
            .chatRoom(user.getChatRoom()).user(user).type(ChatMessageType.USER)
            .content(request.getContent()).build();

        // 메시지 저장
        ChatMessage savedChatMessage = chatMessageRepository.save(newChatMessage);

        // AI 답변
        String aiMessage = geminiService.callGemini(savedChatMessage.getContent());
        ChatMessage aiChatMessage = ChatMessage.builder()
            .chatRoom(user.getChatRoom()).user(null).type(ChatMessageType.AI).content(aiMessage)
            .build();

        // AI 답변 메시지 저장
        ChatMessage savedAIChatMessage = chatMessageRepository.save(aiChatMessage);

        return ChatMessageAddResponse.builder()
            .sendMessage(savedChatMessage.getContent()).sendMessageId(savedChatMessage.getId())
            .aiMessage(savedAIChatMessage.getContent()).aiMessageId(savedAIChatMessage.getId())
            .build();
    }
}
