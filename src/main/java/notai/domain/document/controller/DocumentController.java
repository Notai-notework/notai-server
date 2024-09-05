package notai.domain.document.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import notai.domain.document.dto.request.DocumentAddRequest;
import notai.domain.document.dto.request.DocumentModifyRequest;
import notai.domain.document.dto.request.DocumentRemoveRequest;
import notai.domain.document.dto.response.DocumentDetailResponse;
import notai.domain.document.service.DocumentService;
import notai.global.auth.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // 문서 전체 목록
    @GetMapping
    public ResponseEntity<List<DocumentDetailResponse>> documentList() {

        List<DocumentDetailResponse> response = documentService.findAllDocument();

        return ResponseEntity.ok().body(response);
    }

    // 문서 공유(등록)
    @PostMapping
    public ResponseEntity<DocumentDetailResponse> documentAdd(
        @Valid @ModelAttribute DocumentAddRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        DocumentDetailResponse response = documentService.addDocument(request,
            customUserDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 문서 수정
    @PatchMapping
    public ResponseEntity<DocumentDetailResponse> documentModify(
        @Valid @RequestBody DocumentModifyRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        DocumentDetailResponse response = documentService.modifyDocument(request,
            customUserDetails.getUser());

        return ResponseEntity.ok().body(response);
    }

    // 문서 삭제
    @DeleteMapping
    public ResponseEntity<Void> documentRemove(@Valid @RequestBody DocumentRemoveRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        documentService.removeDocument(request, customUserDetails.getUser());

        return ResponseEntity.noContent().build();
    }
}
