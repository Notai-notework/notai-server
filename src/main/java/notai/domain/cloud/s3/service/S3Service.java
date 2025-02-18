package notai.domain.cloud.s3.service;

import java.io.IOException;
import notai.domain.cloud.s3.dto.UploadResponse;
import notai.domain.file.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    UploadResponse uploadFile(MultipartFile multipartFile, FileType fileType) throws IOException;

    void deleteFile(String fileName);
}
