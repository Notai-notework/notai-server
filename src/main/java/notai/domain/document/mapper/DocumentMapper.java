package notai.domain.document.mapper;

import notai.domain.document.dto.response.DocumentDetailResponse;
import notai.domain.document.entity.Document;
import notai.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(source = "documentFile.url", target = "documentFileUrl")
    @Mapping(source = "imageFile.url", target = "previewImageUrl")
    @Mapping(source = "user", target = "user", qualifiedByName = "toSummaryDTO")
    @Mapping(source = "documentTag.tag.name", target = "tagName")
    DocumentDetailResponse toDetailDTO(Document document);
}
