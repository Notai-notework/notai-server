package notai.domain.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordCheckRequest {
    private String password;
}
