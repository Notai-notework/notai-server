package notai.domain.scrap.mapper;

import notai.domain.document.mapper.DocumentMapper;
import notai.domain.scrap.dto.response.ScrapDetailResponse;
import notai.domain.scrap.entity.Scrap;
import notai.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class,
    DocumentMapper.class})
public interface ScrapMapper {

    ScrapMapper INSTANCE = Mappers.getMapper(ScrapMapper.class);

    @Mapping(source = "user", target = "user", qualifiedByName = "toSummaryDTO")
    @Mapping(source = "document", target="document", qualifiedByName = "toDetailDTO")
    ScrapDetailResponse toDetailDTO(Scrap scrap);
}
