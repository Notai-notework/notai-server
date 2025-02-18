package notai.domain.user.dto.response;

import lombok.Data;

@Data
public class UserSummaryResponse {
    private Long id;
    private String email;
    private String nickname;
}
