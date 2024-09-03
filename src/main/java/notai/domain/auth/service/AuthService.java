package notai.domain.auth.service;

import lombok.RequiredArgsConstructor;
import notai.domain.auth.dto.request.EmailCheckRequest;
import notai.domain.auth.dto.request.NicknameCheckRequest;
import notai.domain.auth.dto.request.PasswordCheckRequest;
import notai.domain.auth.dto.request.RegisterRequest;
import notai.domain.auth.dto.response.MessageResponse;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.entity.User;
import notai.domain.user.mapper.UserMapper;
import notai.domain.user.repository.UserRepository;
import notai.global.enums.Role;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.AuthErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserDetailResponse registerUser(RegisterRequest request) {

        User user = UserMapper.INSTANCE.toEntity(request);
        user.updateRole(Role.USER);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDetail(savedUser);
    }

    // 비밀번호 중복 확인
    public MessageResponse checkPassword(PasswordCheckRequest request) {

        Boolean isExist = userRepository.existsByPassword(
            passwordEncoder.encode(request.getPassword()));

        // 비밀번호 중복
        if (isExist) {
            throw new CustomException(AuthErrorCode.AUTH_PASSWORD_DUPLICATION);
        }

        return generateMessageResponse("사용 가능한 비밀번호입니다");
    }

    // 별명 중복 확인
    public MessageResponse checkNickname(NicknameCheckRequest request) {

        Boolean isExist = userRepository.existsByNickname(request.getNickname());

        // 닉네임 중복
        if (isExist) {
            throw new CustomException(AuthErrorCode.AUTH_NICKNAME_DUPLICATION);
        }

        return generateMessageResponse("사용 가능한 별명입니다");
    }

    // 이메일 중복 확인
    public MessageResponse checkEmail(EmailCheckRequest request) {

        Boolean isExist = userRepository.existsByEmail(request.getEmail());

        if (isExist) {
            throw new CustomException(AuthErrorCode.AUTH_EMAIL_DUPLICATION);
        }

        return generateMessageResponse("사용 가능한 이메일입니다");
    }

    // MessageResponse 생성
    private MessageResponse generateMessageResponse(String message) {
        return MessageResponse.builder().message(message).build();
    }
}
