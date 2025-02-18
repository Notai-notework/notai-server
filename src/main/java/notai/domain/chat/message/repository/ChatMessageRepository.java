package notai.domain.chat.message.repository;

import java.util.List;
import notai.domain.chat.message.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomId(Long chatRoomId);
}
