package notai.domain.scrap.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import notai.domain.document.entity.Document;
import notai.domain.document.repository.DocumentRepository;
import notai.domain.scrap.dto.response.ScrapDetailResponse;
import notai.domain.scrap.entity.Scrap;
import notai.domain.scrap.mapper.ScrapMapper;
import notai.domain.scrap.repository.ScrapRepository;
import notai.domain.user.entity.User;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.DocumentErrorCode;
import notai.global.exception.errorCode.ScrapErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final ScrapRepository scrapRepository;
    private final DocumentRepository documentRepository;

    // 스크랩한 문서 조회
    @Override
    public List<ScrapDetailResponse> findAllScrap(User user) {

        List<Scrap> foundScrapList = scrapRepository.findByUserId(user.getId());

        return foundScrapList.stream()
            .map(ScrapMapper.INSTANCE::toDetailDTO).toList();
    }

    // 스크랩 추가
    @Override
    public ScrapDetailResponse addScrap(Long documentId, User user) {

        Document foundDocument = documentRepository.findById(documentId)
            .orElseThrow(() -> new CustomException(
                DocumentErrorCode.DOCUMENT_NOT_FOUND));

        // 이미 스크랩 한 경우
        if (scrapRepository.existsByDocumentIdAndUserId(documentId, user.getId())) {
            throw new CustomException(ScrapErrorCode.SCRAP_ALREADY);
        }

        Scrap newScrap = Scrap.builder().user(user).document(foundDocument).build();

        Scrap savedScrap = scrapRepository.save(newScrap);

        return ScrapMapper.INSTANCE.toDetailDTO(savedScrap);
    }

    // 스크랩 삭제
    @Override
    public void removeScrap(Long scrapId, User user) {

        Scrap foundScrap = scrapRepository.findById(scrapId)
            .orElseThrow(
                () -> new CustomException(ScrapErrorCode.SCRAP_NOT_FOUND));

        // 스크랩 당사자가 아닌 경우
        if (!Objects.equals(foundScrap.getUser().getId(), user.getId())) {
            throw new CustomException(ScrapErrorCode.SCRAP_NOT_POSSESSION);
        }

        scrapRepository.delete(foundScrap);
    }
}
