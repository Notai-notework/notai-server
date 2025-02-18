package notai.domain.document.service;

import java.util.List;
import notai.domain.document.dto.request.DocumentAddRequest;
import notai.domain.document.dto.request.DocumentModifyRequest;
import notai.domain.document.dto.request.DocumentRemoveRequest;
import notai.domain.document.dto.response.DocumentDetailResponse;
import notai.domain.user.entity.User;

public interface DocumentService {

    List<DocumentDetailResponse> findAllDocument();

    DocumentDetailResponse addDocument(DocumentAddRequest request, User user);

    DocumentDetailResponse modifyDocument(DocumentModifyRequest request, User user);

    void removeDocument(DocumentRemoveRequest request, User user);
}
