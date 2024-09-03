package notai.domain.user.mapper;

import lombok.RequiredArgsConstructor;
import notai.domain.auth.dto.request.RegisterRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.dto.response.UserModifyPasswordResponse;
import notai.domain.user.dto.response.UserModifyResponse;
import notai.domain.user.dto.response.UserSummaryResponse;
import notai.domain.user.entity.User;
import notai.global.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(RegisterRequest registerRequest);

    UserModifyResponse toModifyDTO(User user);

    UserDetailResponse toDetailDTO(User user);

    UserModifyPasswordResponse toModifyPasswordDTO(User user);

    @Mapping(source = "role", target = "role", qualifiedByName = "setRole")
    UserDetailResponse toDetail(User user);

    @Named("setRole")
    static String setRole(Role role) {
        return role.name();
    }
}
