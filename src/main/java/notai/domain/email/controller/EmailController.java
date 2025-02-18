package notai.domain.email.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notai.domain.email.dto.request.EmailCheckCodeRequest;
import notai.domain.email.service.EmailService;
import notai.global.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    // 이메일 인증코드 요청
    @PostMapping("/email-code")
    public ResponseEntity<Void> emailCodeSend(@RequestParam String email) {

        emailService.sendEmailCode(email);

        return ResponseEntity.ok().build();
    }

    // 이메일 인증코드 확인
    @PostMapping("/email-code-check")
    public ResponseEntity<MessageResponse> emailCodeCheck(
        @Valid @RequestBody EmailCheckCodeRequest request) {

        emailService.checkEmailCode(request);

        MessageResponse response = MessageResponse.builder().message("인증번호가 일치합니다").build();

        return ResponseEntity.ok().body(response);
    }
}
