package notai.domain.chat.message.mapper;

import notai.domain.chat.message.dto.response.ChatMessageDetailResponse;
import notai.domain.chat.message.entity.ChatMessage;
import notai.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(source = "user", target = "user", qualifiedByName = "toSummaryDTO")
    ChatMessageDetailResponse toDetailDTO(ChatMessage chatMessage);
}
