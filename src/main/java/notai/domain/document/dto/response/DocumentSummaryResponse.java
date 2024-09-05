package notai.domain.document.dto.response;

import lombok.Data;
import notai.domain.user.dto.response.UserSummaryResponse;

@Data
public class DocumentSummaryResponse {
    private Long id;
    private String title;
    private UserSummaryResponse user;
}
