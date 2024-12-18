package org.durcit.be.upload.service.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.service.PostService;
import org.durcit.be.system.exception.upload.FileSizeExccedsMaximumLimitException;
import org.durcit.be.system.exception.upload.ImageNotFoundException;
import org.durcit.be.system.exception.upload.S3UploadException;
import org.durcit.be.upload.domain.Images;
import org.durcit.be.upload.dto.UploadRequest;
import org.durcit.be.upload.dto.UploadUpdateRequest;
import org.durcit.be.upload.repository.ImagesRepository;
import org.durcit.be.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.durcit.be.system.exception.ExceptionMessage.*;
import static org.durcit.be.upload.util.UploadUtil.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements UploadService {

    private final AmazonS3 amazonS3;
    private final PostService postService;
    private final ImagesRepository imagesRepository;

    @Value("${custom.s3.bucket-name:burcit}")
    private String bucketName;

    @Value("${custom.s3.presigned-url.expiration-time}")
    private int presignedUrlExpirationMinutes;

    @Override
    @Transactional
    public void upload(UploadRequest request) {
        uploadFiles(request.getPostId(), request.getFiles());
    }

    @Transactional
    public void deleteImages(List<Long> imageIds) {
        if (imageIds != null && !imageIds.isEmpty()) {
            for (Long imageId : imageIds) {
                Images existingImage = imagesRepository.findById(imageId)
                        .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND_ERROR));

                String existingFileKey = extractFileKeyFromUrl(existingImage.getUrl());
                amazonS3.deleteObject(bucketName, existingFileKey);

                imagesRepository.delete(existingImage);
            }
        }
    }

    @Transactional
    public void updateImages(UploadUpdateRequest request) {
        deleteImages(request.getImageIdsToDelete());
        uploadFiles(request.getPostId(), request.getNewFiles());
    }

    @Transactional
    protected void uploadFiles(Long postId, List<MultipartFile> files) {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    validateFileSize(file);
                    try {
                        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());

                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(file.getSize());
                        metadata.setContentType(file.getContentType());

                        amazonS3.putObject(bucketName, uniqueFileName, file.getInputStream(), metadata);

                        String s3Url = generatePresignedUrl(bucketName, uniqueFileName);

                        Images image = Images.builder()
                                .post(postService.getById(postId))
                                .url(s3Url)
                                .originalFilename(file.getOriginalFilename())
                                .build();

                        imagesRepository.save(image);

                    } catch (IOException e) {
                        log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                        throw new S3UploadException(S3_UPLOAD_ERROR);
                    }
                }
            }
        }
    }

    private String generatePresignedUrl(String bucketName, String fileName) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(Date.from(Instant.now().plusSeconds(presignedUrlExpirationMinutes * 60L)));

        URL presignedUrl = amazonS3.generatePresignedUrl(request);
        return presignedUrl.toString();
    }

}
