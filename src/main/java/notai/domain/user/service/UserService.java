package notai.domain.user.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import notai.domain.cloud.s3.dto.UploadResponse;
import notai.domain.cloud.s3.service.S3Service;
import notai.domain.file.entity.File;
import notai.domain.file.enums.FileType;
import notai.domain.user.dto.request.UserModifyPasswordRequest;
import notai.domain.user.dto.request.UserModifyRequest;
import notai.domain.user.dto.request.UserPasswordCheckRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.dto.response.UserModifyPasswordResponse;
import notai.domain.user.dto.response.UserModifyProfileImageResponse;
import notai.domain.user.dto.response.UserModifyResponse;
import notai.domain.user.entity.User;
import notai.domain.user.mapper.UserMapper;
import notai.domain.user.repository.UserRepository;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.FileErrorCode;
import notai.global.exception.errorCode.UserErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    public UserDetailResponse findUser(User user) {
        return UserMapper.INSTANCE.toDetailDTO(user);
    }

    // 유저 정보 수정
    public UserModifyResponse modifyUser(UserModifyRequest request, User user) {

        user.updateAddress(request.getAddress());
        user.updatePhoneNumber(request.getPhoneNumber());
        user.updateNickname(request.getNickname());

        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toModifyDTO(updatedUser);
    }

    // 유저 비밀번호 변경
    public UserModifyPasswordResponse modifyUserPassword(UserModifyPasswordRequest request, User user) {

        user.updatePassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toModifyPasswordDTO(updatedUser);
    }

    // 유저 현재 비밀번호 확인
    public void checkUserPassword(UserPasswordCheckRequest request, User user) {

        boolean isEqual = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isEqual) {
            throw new CustomException(UserErrorCode.USER_PASSWORD_NOT_CORRECT);
        }
    }

    // 유저 프로필 수정/추가
    public UserModifyProfileImageResponse modifyUserProfileImage(
        MultipartFile image, User user) {

        UploadResponse uploadResponse;

        try {
            uploadResponse = s3Service.uploadFile(image, FileType.IMAGE);
        } catch (IOException e) {
            throw new CustomException(FileErrorCode.FILE_UPLOAD_FAILED);
        }

        File file = File.builder().url(uploadResponse.getFileUrl())
            .name(uploadResponse.getFileName())
            .type(FileType.IMAGE).build();

        // 기존 이미지 Object 삭제
        if (user.getImageFile() != null) {
            s3Service.deleteFile(user.getImageFile().getName());
        }

        user.updateImageFile(file);
        userRepository.save(user);

        return UserModifyProfileImageResponse.builder()
            .id(user.getId())
            .imageName(uploadResponse.getFileName())
            .imageUrl(uploadResponse.getFileUrl()).build();
    }

    // 유저 삭제
    public void removeUser(User user) {
        userRepository.delete(user);
    }
}
