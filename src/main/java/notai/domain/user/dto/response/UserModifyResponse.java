package notai.domain.user.dto.response;

import lombok.Data;

@Data
public class UserModifyResponse {
    private Long id;
    private String nickname;
    private String address;
    private String phoneNumber;
}
