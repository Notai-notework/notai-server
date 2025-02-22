package notai.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NicknameCheckRequest {

    @NotBlank(message = "별명은 필수 입력 항목입니다")
    private String nickname;
}
