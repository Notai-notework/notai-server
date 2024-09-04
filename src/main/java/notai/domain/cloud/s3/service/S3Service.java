package notai.domain.cloud.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import notai.domain.cloud.s3.dto.UploadResponse;
import notai.domain.file.enums.FileType;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.FileErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String s3Bucket;

    public S3Service(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String s3Bucket) {
        this.amazonS3 = amazonS3;
        this.s3Bucket = s3Bucket;
    }

    // 파일 업로드
    public UploadResponse uploadFile(MultipartFile multipartFile, FileType fileType) throws IOException {

        File file = converToFile(multipartFile);

        String fileName = (fileType == FileType.IMAGE ? "image/" : "document/") + file.getPath();

        amazonS3.putObject(new PutObjectRequest(s3Bucket, fileName, file).withCannedAcl(
            CannedAccessControlList.PublicRead));

        file.delete();

        UploadResponse response = UploadResponse.builder().fileName(fileName)
            .fileUrl(amazonS3.getUrl(s3Bucket, fileName).toString()).build();

        return response;
    }

    // MultipartFile -> File 변환
    private File converToFile(MultipartFile multipartFile)
        throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();

        assert originalFilename != null;
        String fileName = uuid + "_" + originalFilename.replaceAll("\\s", "_");

        File file = new File(fileName);
        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 오류 발생: {}", e.getMessage());
                throw e;
            }

            return file;
        }

        throw new CustomException(FileErrorCode.FILE_CONVERT_ERROR);
    }

    // 파일 삭제
    public void deleteFile(String fileName) {

        try {
            String decode = URLDecoder.decode(fileName, "UTF-8");
            amazonS3.deleteObject(s3Bucket, decode);
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(FileErrorCode.FILE_REMOVE_FAILED);
        }
    }
}
