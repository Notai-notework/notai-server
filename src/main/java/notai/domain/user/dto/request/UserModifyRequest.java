package notai.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserModifyRequest {

    @NotBlank(message = "별명은 필수 입력 항목입니다")
    private String nickname;

    @NotBlank(message = "주소는 필수 입력 항목입니다")
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 '010-XXXX-XXXX' 형식이어야 합니다")
    private String phoneNumber;
}
