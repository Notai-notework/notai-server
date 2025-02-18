package notai.domain.chat.message.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import notai.domain.chat.message.dto.request.ChatMessageAddRequest;
import notai.domain.chat.message.dto.response.ChatMessageAddResponse;
import notai.domain.chat.message.dto.response.ChatMessageDetailResponse;
import notai.domain.chat.message.service.ChatMessageService;
import notai.global.auth.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-rooms/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 채팅 메시지 목록
    @GetMapping
    public ResponseEntity<List<ChatMessageDetailResponse>> chatMessageListDetails(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<ChatMessageDetailResponse> response = chatMessageService.findAllChatMessage(
            customUserDetails.getUser());

        return ResponseEntity.ok().body(response);
    }

    // 채팅 메시지 전송 및 AI 답변 요청
    @PostMapping
    public ResponseEntity<ChatMessageAddResponse> chatMessageAdd(
        @Valid @RequestBody ChatMessageAddRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        ChatMessageAddResponse response = chatMessageService.addChatMessage(request,
            customUserDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
