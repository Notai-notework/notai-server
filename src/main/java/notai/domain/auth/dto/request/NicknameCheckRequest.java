package notai.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NicknameCheckRequest {

    @NotBlank(message = "별명은 필수 입력 항목입니다")
    private String nickname;
}
