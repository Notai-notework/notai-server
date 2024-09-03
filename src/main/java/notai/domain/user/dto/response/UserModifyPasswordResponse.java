package notai.domain.user.dto.response;

import lombok.Data;

@Data
public class UserModifyPasswordResponse {
    private Long id;
    private String email;
    private String password;
}
