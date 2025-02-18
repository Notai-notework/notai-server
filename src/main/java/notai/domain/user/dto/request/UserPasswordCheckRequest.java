package notai.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordCheckRequest {

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    private String password;
}
