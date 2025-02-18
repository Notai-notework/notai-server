package notai.domain.user.dto.response;

import lombok.Data;

@Data
public class UserDetailResponse {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String phoneNumber;
    private String nickname;
    private String address;
    private String imageUrl;
}
