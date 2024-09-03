package notai.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "올바르지 않은 이메일 형식입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 '010-XXXX-XXXX' 형식 이어야 합니다")
    private String phoneNumber;

    @NotBlank(message = "주소는 필수 입력 항목입니다")
    private String address;

    @NotBlank(message = "이름은 필수 입력 항목입니다")
    private String name;

    @NotBlank(message = "별명은 필수 입력 항목입니다")
    private String nickname;
}
