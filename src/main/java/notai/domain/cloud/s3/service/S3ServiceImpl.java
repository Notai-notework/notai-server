package notai.domain.cloud.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import notai.domain.cloud.s3.dto.UploadResponse;
import notai.domain.file.enums.FileType;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.FileErrorCode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;
    private final String s3Bucket;

    public S3ServiceImpl(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String s3Bucket) {
        this.amazonS3 = amazonS3;
        this.s3Bucket = s3Bucket;
    }

    // 파일 업로드
    @Override
    public UploadResponse uploadFile(MultipartFile multipartFile, FileType fileType)
        throws IOException {

        File file = converToFile(multipartFile);

        String uuid = UUID.randomUUID().toString();

        String fileName =
            (fileType == FileType.IMAGE ? "image/" : "document/") + uuid + "/" + file.getPath();

        amazonS3.putObject(new PutObjectRequest(s3Bucket, fileName, file).withCannedAcl(
            CannedAccessControlList.PublicRead));

        UploadResponse response = UploadResponse.builder().build();

        // 미리보기 이미지 업로드
        if (fileType == FileType.DOCUMENT) {
            uploadPdfFirstPageToImage(multipartFile.getInputStream(), uuid, response);
        }

        response.setFileName(fileName);
        response.setFileUrl(amazonS3.getUrl(s3Bucket, fileName).toString());

        // 로컬 파일 제거
        file.delete();

        return response;
    }

    // MultipartFile -> File 변환
    private File converToFile(MultipartFile multipartFile)
        throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();

        assert originalFilename != null;
        File file = new File(originalFilename);
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
    @Override
    public void deleteFile(String fileName) {

        try {
            String decode = URLDecoder.decode(fileName, "UTF-8");
            amazonS3.deleteObject(s3Bucket, decode);
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(FileErrorCode.FILE_REMOVE_FAILED);
        }
    }

    // PDF 문서 첫 표지를 이미지로 추출 후 업로드
    private void uploadPdfFirstPageToImage(InputStream pdfInputStream, String uuid,
        UploadResponse response) throws IOException {

        byte[] imageBytes;

        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            imageBytes = baos.toByteArray();
        }

        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageBytes.length);
        objectMetadata.setContentType("image/jpg");

        String fileName = "document/" + uuid + "/preview-image.jpg";
        amazonS3.putObject(s3Bucket, fileName, inputStream, objectMetadata);

        response.setPreviewFileName(fileName);
        response.setPreviewFileUrl(amazonS3.getUrl(s3Bucket, fileName).toString());
    }
}
