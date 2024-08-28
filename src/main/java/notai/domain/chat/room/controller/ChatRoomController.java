package notai.domain.chat.room.controller;

import lombok.RequiredArgsConstructor;
import notai.domain.chat.room.dto.ChatRoomResponseDTO;
import notai.domain.chat.room.service.ChatRoomService;
import notai.global.auth.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ChatRoomResponseDTO> chatRoomAdd(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ChatRoomResponseDTO response = chatRoomService.addChatRoom(customUserDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 채팅방 삭제(종료)
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> chatRoomRemove(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable Long roomId) {

        chatRoomService.removeChatRoom(roomId, customUserDetails.getUser());

        return ResponseEntity.noContent().build();
    }
}
