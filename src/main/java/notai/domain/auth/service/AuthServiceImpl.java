package notai.domain.auth.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import notai.domain.auth.dto.request.EmailCheckRequest;
import notai.domain.auth.dto.request.NicknameCheckRequest;
import notai.domain.auth.dto.request.PasswordCheckRequest;
import notai.domain.auth.dto.request.RegisterRequest;
import notai.global.auth.jwt.JwtTokenProvider;
import notai.global.dto.MessageResponse;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.entity.User;
import notai.domain.user.mapper.UserMapper;
import notai.domain.user.repository.UserRepository;
import notai.global.enums.JwtType;
import notai.global.enums.Role;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.AuthErrorCode;
import notai.global.exception.errorCode.JwtErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Override
    public UserDetailResponse registerUser(RegisterRequest request) {

        User user = UserMapper.INSTANCE.toEntity(request);
        user.updateRole(Role.USER);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDetail(savedUser);
    }

    // 비밀번호 중복 확인
    @Override
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
    @Override
    public MessageResponse checkNickname(NicknameCheckRequest request) {

        Boolean isExist = userRepository.existsByNickname(request.getNickname());

        // 닉네임 중복
        if (isExist) {
            throw new CustomException(AuthErrorCode.AUTH_NICKNAME_DUPLICATION);
        }

        return generateMessageResponse("사용 가능한 별명입니다");
    }

    // 이메일 중복 확인
    @Override
    public MessageResponse checkEmail(EmailCheckRequest request) {

        Boolean isExist = userRepository.existsByEmail(request.getEmail());

        if (isExist) {
            throw new CustomException(AuthErrorCode.AUTH_EMAIL_DUPLICATION);
        }

        return generateMessageResponse("사용 가능한 이메일입니다");
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {

        boolean isExpired = jwtTokenProvider.isExpired(refreshToken, JwtType.REFRESH);

        if (!isExpired) {

            Map<String, Object> payload = jwtTokenProvider.extractPayload(refreshToken);

            String access = jwtTokenProvider.generate((String) payload.get("email"),
                (String) payload.get("name"), Role.valueOf((String) payload.get("role")),
                JwtType.ACCESS);
            String refresh = jwtTokenProvider.generate((String) payload.get("email"),
                (String) payload.get("name"), Role.valueOf((String) payload.get("role")),
                JwtType.REFRESH);

            return Map.of("access", access, "refresh", refresh);
        }

        throw new CustomException(JwtErrorCode.REFRESH_TOKEN_INVALID_SIGNATURE);
    }

    // MessageResponse 생성
    private MessageResponse generateMessageResponse(String message) {
        return MessageResponse.builder().message(message).build();
    }
}
