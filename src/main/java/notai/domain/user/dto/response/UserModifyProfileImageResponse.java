package notai.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserModifyProfileImageResponse {
    private Long id;
    private String imageName;
    private String imageUrl;
}
