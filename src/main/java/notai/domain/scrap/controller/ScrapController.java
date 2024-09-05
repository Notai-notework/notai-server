package notai.domain.scrap.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import notai.domain.scrap.dto.response.ScrapDetailResponse;
import notai.domain.scrap.service.ScrapService;
import notai.global.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    // 스크랩 목록 조회
    @GetMapping
    public List<ScrapDetailResponse> scrapList(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return scrapService.findAllScrap(customUserDetails.getUser());
    }

    // 스크랩 추가
    @PostMapping("/{documentId}")
    public ScrapDetailResponse scrapAdd(@PathVariable Long documentId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return scrapService.addScrap(documentId, customUserDetails.getUser());
    }

    // 스크랩 삭제
    @DeleteMapping("/{scrapId}")
    public ResponseEntity<Void> scrapRemove(@PathVariable Long scrapId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        scrapService.removeScrap(scrapId, customUserDetails.getUser());

        return ResponseEntity.noContent().build();
    }
}
