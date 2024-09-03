package notai.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordCheckRequest {

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    private String password;
}
