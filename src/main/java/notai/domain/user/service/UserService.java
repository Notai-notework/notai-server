package notai.domain.user.service;

import lombok.RequiredArgsConstructor;
import notai.domain.user.dto.request.UserModifyPasswordRequest;
import notai.domain.user.dto.request.UserModifyRequest;
import notai.domain.user.dto.request.UserPasswordCheckRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.dto.response.UserModifyPasswordResponse;
import notai.domain.user.dto.response.UserModifyResponse;
import notai.domain.user.entity.User;
import notai.domain.user.mapper.UserMapper;
import notai.domain.user.repository.UserRepository;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.UserErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailResponse findUser(Long userId) {

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
            new CustomException(UserErrorCode.USER_NOT_FOUND));

        return UserMapper.INSTANCE.toDetailDTO(foundUser);
    }

    // 유저 정보 수정
    public UserModifyResponse modifyUser(UserModifyRequest request, Long userId) {

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
            new CustomException(UserErrorCode.USER_NOT_FOUND));

        foundUser.updateAddress(request.getAddress());
        foundUser.updatePhoneNumber(request.getPhoneNumber());
        foundUser.updateNickname(request.getNickname());

        User updatedUser = userRepository.save(foundUser);

        return UserMapper.INSTANCE.toModifyDTO(updatedUser);
    }

    // 유저 비밀번호 변경
    public UserModifyPasswordResponse modifyUserPassword(UserModifyPasswordRequest request, Long userId) {

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
            new CustomException(UserErrorCode.USER_NOT_FOUND));

        foundUser.updatePassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(foundUser);

        return UserMapper.INSTANCE.toModifyPasswordDTO(updatedUser);
    }

    // 유저 현재 비밀번호 확인
    public void checkUserPassword(UserPasswordCheckRequest request, User user) {

        boolean isEqual = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isEqual) {
            throw new CustomException(UserErrorCode.USER_PASSWORD_NOT_CORRECT);
        }
    }

    // 유저 삭제
    public void removeUser(User user) {
        userRepository.delete(user);
    }
}
