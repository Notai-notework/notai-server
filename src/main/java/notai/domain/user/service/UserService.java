package notai.domain.user.service;

import notai.domain.user.dto.request.UserModifyPasswordRequest;
import notai.domain.user.dto.request.UserModifyRequest;
import notai.domain.user.dto.request.UserPasswordCheckRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.dto.response.UserModifyPasswordResponse;
import notai.domain.user.dto.response.UserModifyProfileImageResponse;
import notai.domain.user.dto.response.UserModifyResponse;
import notai.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDetailResponse findUser(User user);

    UserModifyResponse modifyUser(UserModifyRequest request, User user);

    UserModifyPasswordResponse modifyUserPassword(UserModifyPasswordRequest request, User user);

    void checkUserPassword(UserPasswordCheckRequest request, User user);

    UserModifyProfileImageResponse modifyUserProfileImage(
        MultipartFile image, User user);

    void removeUser(User user);
}
