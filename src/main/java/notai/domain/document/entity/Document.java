package notai.domain.document.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.domain.document.dto.request.DocumentModifyRequest;
import notai.domain.file.entity.File;
import notai.domain.user.entity.User;
import notai.global.entity.BaseTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Document extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 45)
    private String title;

    private String content;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_file_id")
    private File imageFile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_file_id")
    private File documentFile;

    @OneToOne(mappedBy = "document", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private DocumentTag documentTag;

    public void updateConfig(DocumentModifyRequest request) {
        title = request.getTitle();
        content = request.getContent();
    }

    public void updateImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public void updateDocumentFile(File documentFile) {
        this.documentFile = documentFile;
    }
}
