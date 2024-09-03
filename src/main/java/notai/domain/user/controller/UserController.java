package notai.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notai.domain.user.dto.request.UserModifyPasswordRequest;
import notai.domain.user.dto.request.UserModifyRequest;
import notai.domain.user.dto.request.UserPasswordCheckRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.dto.response.UserModifyPasswordResponse;
import notai.domain.user.dto.response.UserModifyResponse;
import notai.domain.user.service.UserService;
import notai.global.auth.CustomUserDetails;
import notai.global.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 상세 정보(내정보) 요청
    @GetMapping
    public ResponseEntity<UserDetailResponse> userDetails(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UserDetailResponse response = userService.findUser(customUserDetails.getUser());

        return ResponseEntity.ok().body(response);
    }

    // 유저 정보 수정
    @PatchMapping
    public ResponseEntity<UserModifyResponse> userModify(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody UserModifyRequest request) {

        UserModifyResponse response = userService.modifyUser(request, customUserDetails.getUser());

        return ResponseEntity.ok().body(response);
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<UserModifyPasswordResponse> userModifyPassword(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody UserModifyPasswordRequest request) {

        UserModifyPasswordResponse response = userService.modifyUserPassword(request,
            customUserDetails.getUser());

        return ResponseEntity.ok().body(response);
    }

    // 현재 비밀번호 확인
    @PostMapping("/password-check")
    public ResponseEntity<MessageResponse> userPasswordCheck(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody UserPasswordCheckRequest request) {

        userService.checkUserPassword(request, customUserDetails.getUser());

        MessageResponse response = MessageResponse.builder().message("올바른 비밀번호입니다").build();

        return ResponseEntity.ok().body(response);
    }

    // 유저 삭제
    @DeleteMapping
    public ResponseEntity<Void> userRemove(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.removeUser(customUserDetails.getUser());

        return ResponseEntity.noContent().build();
    }
}
