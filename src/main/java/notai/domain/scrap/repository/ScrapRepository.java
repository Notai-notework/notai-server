package notai.domain.scrap.repository;

import java.util.List;
import notai.domain.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    List<Scrap> findByUserId(Long userId);

    Boolean existsByDocumentIdAndUserId(Long documentId, Long userId);
}
