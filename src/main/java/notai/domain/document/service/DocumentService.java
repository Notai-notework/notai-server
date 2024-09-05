package notai.domain.document.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import notai.domain.cloud.s3.dto.UploadResponse;
import notai.domain.cloud.s3.service.S3Service;
import notai.domain.document.dto.request.DocumentAddRequest;
import notai.domain.document.dto.request.DocumentModifyRequest;
import notai.domain.document.dto.request.DocumentRemoveRequest;
import notai.domain.document.dto.response.DocumentDetailResponse;
import notai.domain.document.entity.Document;
import notai.domain.document.entity.DocumentTag;
import notai.domain.document.mapper.DocumentMapper;
import notai.domain.document.repository.DocumentRepository;
import notai.domain.file.entity.File;
import notai.domain.file.enums.FileType;
import notai.domain.user.entity.User;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.DocumentErrorCode;
import notai.global.exception.errorCode.FileErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentTagService documentTagService;
    private final S3Service s3Service;

    // 전체 문서 조회
    public List<DocumentDetailResponse> findAllDocument() {

        return documentRepository.findAll().stream()
            .map(DocumentMapper.INSTANCE::toDetailDTO).toList();
    }

    // 문서 등록(공유)
    public DocumentDetailResponse addDocument(DocumentAddRequest request, User user) {

        Document newDocument = Document.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .user(user).build();

        UploadResponse uploadResponse;
        try {
            uploadResponse = s3Service.uploadFile(request.getDocumentFile(), FileType.DOCUMENT);
        } catch (IOException e) {
            throw new CustomException(FileErrorCode.FILE_UPLOAD_FAILED);
        }

        File documentFile = File.builder().name(uploadResponse.getFileName())
            .url(uploadResponse.getFileUrl())
            .type(FileType.DOCUMENT).build();
        File previewImageFile = File.builder().name(uploadResponse.getPreviewFileName())
            .url(uploadResponse.getPreviewFileUrl()).type(FileType.IMAGE).build();

        newDocument.updateDocumentFile(documentFile);
        newDocument.updateImageFile(previewImageFile);

        // 문서 및 태그 저장
        DocumentTag savedDocumentTag = documentTagService.addTagInDocument(newDocument,
            request.getTagName());

        return DocumentMapper.INSTANCE.toDetailDTO(savedDocumentTag.getDocument());
    }

    // 문서 수정
    public DocumentDetailResponse modifyDocument(DocumentModifyRequest request, User user) {

        Document foundDocument = documentRepository.findById(request.getDocumentId())
            .orElseThrow(() -> new CustomException(
                DocumentErrorCode.DOCUMENT_NOT_FOUND));

        // 문서 주인이 아닌 경우
        if (!Objects.equals(foundDocument.getUser().getId(), user.getId())) {
            throw new CustomException(DocumentErrorCode.DOCUMENT_NOT_POSSESSION);
        }

        foundDocument.updateConfig(request);

        DocumentTag documentTag = documentTagService.modifyTagInDocument(foundDocument,
            request.getTagName());

        return DocumentMapper.INSTANCE.toDetailDTO(documentTag.getDocument());
    }

    public void removeDocument(DocumentRemoveRequest request, User user) {

        Document foundDocument = documentRepository.findById(request.getDocumentId())
            .orElseThrow(() -> new CustomException(
                DocumentErrorCode.DOCUMENT_NOT_FOUND));

        // 문서 주인이 아닌 경우
        if (!Objects.equals(foundDocument.getUser().getId(), user.getId())) {
            throw new CustomException(DocumentErrorCode.DOCUMENT_NOT_POSSESSION);
        }

        // S3 문서 및 미리보기 이미지 삭제
        s3Service.deleteFile(foundDocument.getDocumentFile().getName());
        s3Service.deleteFile(foundDocument.getImageFile().getName());

        documentRepository.delete(foundDocument);
    }
}
