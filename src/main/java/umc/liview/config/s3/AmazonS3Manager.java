package umc.liview.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.config.AmazonConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonConfig amazonConfig;
    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) {

        String fileName = UUID.randomUUID().toString().concat(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            String imgURL = amazonS3.getUrl(amazonConfig.getBucket(), amazonConfig.getTourPath() + "/" + fileName).toString();
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), amazonConfig.getTourPath() + "/" + fileName, file.getInputStream(), metadata));
            return imgURL;

        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());

        }
        return "";
    }

    public String uploadUserFile(MultipartFile file) {

        String fileName = UUID.randomUUID().toString().concat(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            String imgURL = amazonS3.getUrl(amazonConfig.getBucket(), amazonConfig.getUserPath() + "/" + fileName).toString();
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), amazonConfig.getTourPath() + "/" + fileName, file.getInputStream(), metadata));
            return imgURL;

        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());

        }
        return "";
    }
}




