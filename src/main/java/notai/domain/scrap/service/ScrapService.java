package notai.domain.scrap.service;

import java.util.List;
import notai.domain.scrap.dto.response.ScrapDetailResponse;
import notai.domain.user.entity.User;

public interface ScrapService {

    List<ScrapDetailResponse> findAllScrap(User user);

    ScrapDetailResponse addScrap(Long documentId, User user);

    void removeScrap(Long scrapId, User user);

}
