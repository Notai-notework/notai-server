package notai.domain.chat.room.repository;

import java.util.Optional;
import notai.domain.chat.room.entity.ChatRoom;
import notai.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByUserId(Long userId);
}
