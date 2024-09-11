package notai.domain.cloud.s3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponse {
    private String fileName;
    private String fileUrl;
    private String previewFileName;
    private String previewFileUrl;
}
