package notai.domain.document.repository;

import java.util.Optional;
import notai.domain.document.entity.DocumentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTagRepository extends JpaRepository<DocumentTag, Long> {

    Optional<DocumentTag> findByDocumentId(Long documentId);
}
