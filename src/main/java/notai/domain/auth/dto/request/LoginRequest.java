package notai.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "올바르지 않은 이메일 형식입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    private String password;
}
