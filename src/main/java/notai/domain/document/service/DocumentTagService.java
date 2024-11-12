package notai.domain.document.service;

import notai.domain.document.entity.Document;
import notai.domain.document.entity.DocumentTag;

public interface DocumentTagService {

    DocumentTag addTagInDocument(Document document, String tagName);

    DocumentTag modifyTagInDocument(Document document, String tagName);
}
