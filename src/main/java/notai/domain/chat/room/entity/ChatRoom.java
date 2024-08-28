package notai.domain.chat.room.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.domain.chat.message.entity.ChatMessage;
import notai.domain.chat.room.dto.ChatRoomResponseDTO;
import notai.domain.user.entity.User;
import notai.global.entity.BaseTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ChatMessage> chatMessages = new ArrayList<>();

    // entity -> dto 변환
    public ChatRoomResponseDTO toDTO() {
        return ChatRoomResponseDTO.builder().id(id)
            .build();
    }

    // 채팅방 멤버 여부
    public boolean isMember(Long userId) {
        return Objects.equals(user.getId(), userId);
    }
}
