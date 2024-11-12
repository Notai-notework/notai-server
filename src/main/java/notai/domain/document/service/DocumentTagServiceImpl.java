package notai.domain.document.service;

import lombok.RequiredArgsConstructor;
import notai.domain.document.entity.Document;
import notai.domain.document.entity.DocumentTag;
import notai.domain.document.repository.DocumentTagRepository;
import notai.domain.tag.entity.Tag;
import notai.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentTagServiceImpl implements DocumentTagService {

    private final DocumentTagRepository documentTagRepository;
    private final TagRepository tagRepository;

    // 새 문서에 태그 추가
    @Override
    @Transactional
    public DocumentTag addTagInDocument(Document document, String tagName) {

        // 태그 데이터 가져오기
        Tag foundTag = tagRepository.findByName(tagName)
            .orElse(Tag.builder().name(tagName).build()); // 태그 데이터가 없으면 새로 생성

        DocumentTag newDocumentTag = DocumentTag.builder().tag(foundTag).build();
        newDocumentTag.updateDocument(document);

        return documentTagRepository.save(newDocumentTag);
    }

    // 문서 태그 수정
    @Override
    @Transactional
    public DocumentTag modifyTagInDocument(Document document, String tagName) {

        // 태그 데이터 가져오기
        Tag foundTag = tagRepository.findByName(tagName)
            .orElse(Tag.builder().name(tagName).build()); // 태그 데이터가 없으면 새로 생성

        document.getDocumentTag().updateTag(foundTag);

        return documentTagRepository.save(document.getDocumentTag());
    }
}
