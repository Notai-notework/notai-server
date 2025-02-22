package notai.domain.document.dto.response;

import lombok.Builder;
import lombok.Data;
import notai.domain.user.dto.response.UserSummaryResponse;

@Data
@Builder
public class DocumentSummaryResponse {
    private Long id;
    private String title;
    private UserSummaryResponse user;
}
