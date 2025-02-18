package notai.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerifyRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "올바르지 않은 이메일 형식입니다")
    private String email;

    @NotNull(message = "인증번호는 필수 입력 항목입니다")
    private Integer number;
}
