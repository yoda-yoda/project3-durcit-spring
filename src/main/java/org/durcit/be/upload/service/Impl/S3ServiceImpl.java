package org.durcit.be.upload.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.service.PostService;
import org.durcit.be.system.exception.upload.S3UploadException;
import org.durcit.be.upload.domain.Images;
import org.durcit.be.upload.dto.UploadRequest;
import org.durcit.be.upload.repository.ImagesRepository;
import org.durcit.be.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.durcit.be.system.exception.ExceptionMessage.S3_UPLOAD_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements UploadService {

    private final AmazonS3 amazonS3;
    private final PostService postService;
    private final ImagesRepository imagesRepository;

    @Value("${custom.s3.bucket-name}")
    private String bucketName;

    public void upload(UploadRequest request) {
        for (MultipartFile file : request.getFiles()) {
            if (!file.isEmpty()) {
                try {
                    String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(file.getSize());
                    metadata.setContentType(file.getContentType());

                    amazonS3.putObject(bucketName, uniqueFileName, file.getInputStream(), metadata);

                    String s3Url = amazonS3.getUrl(bucketName, uniqueFileName).toString();

                    Images image = Images.builder()
                            .post(postService.getById(request.getPostId()))
                            .url(s3Url)
                            .build();

                    imagesRepository.save(image);

                } catch (IOException e) {
                    throw new S3UploadException(S3_UPLOAD_ERROR);
                }
            }
        }
    }


    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}
