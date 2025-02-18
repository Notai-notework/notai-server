package notai.domain.scrap.dto.response;

import lombok.Data;
import notai.domain.document.dto.response.DocumentDetailResponse;
import notai.domain.user.dto.response.UserSummaryResponse;

@Data
public class ScrapDetailResponse {
    private Long id;
    private UserSummaryResponse user;
    private DocumentDetailResponse document;
}
