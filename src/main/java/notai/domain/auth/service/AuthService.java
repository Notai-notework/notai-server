package notai.domain.auth.service;

import java.util.Map;
import notai.domain.auth.dto.request.EmailCheckRequest;
import notai.domain.auth.dto.request.NicknameCheckRequest;
import notai.domain.auth.dto.request.PasswordCheckRequest;
import notai.domain.auth.dto.request.RegisterRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.global.dto.MessageResponse;

public interface AuthService {

    UserDetailResponse registerUser(RegisterRequest request);

    MessageResponse checkPassword(PasswordCheckRequest request);

    MessageResponse checkNickname(NicknameCheckRequest request);

    MessageResponse checkEmail(EmailCheckRequest request);

    Map<String, String> refreshToken(String refreshToken);
}
