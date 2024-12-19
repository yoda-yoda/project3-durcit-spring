package org.durcit.be.upload.service.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.repository.MemberRepository;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.exception.upload.S3UploadException;
import org.durcit.be.upload.dto.ProfileImageRequest;
import org.durcit.be.upload.service.ProfileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

import static org.durcit.be.security.util.ProfileImageUtil.generateRandomProfileImage;
import static org.durcit.be.system.exception.ExceptionMessage.S3_UPLOAD_ERROR;
import static org.durcit.be.upload.util.UploadUtil.generateUniqueFileName;
import static org.durcit.be.upload.util.UploadUtil.validateFileSize;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class S3ProfileUploadServiceImpl implements ProfileUploadService {

    private final AmazonS3 amazonS3;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Value("${custom.s3.bucket-name:burcit}")
    private String bucketName;

    @Value("${custom.s3.presigned-url.expiration-time}")
    private int presignedUrlExpirationMinutes;

    private static final String PROFILE_IMAGE_FOLDER = "profile-images/";


    public String uploadProfileReturnUrl(ProfileImageRequest profileImageRequest) {
        if (profileImageRequest == null || profileImageRequest.getProfileImage() == null) {
            log.warn("No profile image provided, generating a random profile image.");
            return null;
        }

        MultipartFile profileImage = profileImageRequest.getProfileImage();
        validateFileSize(profileImage);

        try {
            String uniqueFileName = generateUniqueFileName(profileImage.getOriginalFilename());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(profileImage.getSize());
            metadata.setContentType(profileImage.getContentType());
            amazonS3.putObject(new PutObjectRequest(bucketName, PROFILE_IMAGE_FOLDER + uniqueFileName, profileImage.getInputStream(), metadata));
            String url = amazonS3.getUrl(bucketName, PROFILE_IMAGE_FOLDER + uniqueFileName).toString();

            Member member = memberService.getById(SecurityUtil.getCurrentMemberId());
            member.setProfileImage(url);
            memberRepository.save(member);

            return amazonS3.getUrl(bucketName, uniqueFileName).toString();

        } catch (IOException | RuntimeException e) {
            log.error("Failed to upload profile image", e);
            throw new S3UploadException(S3_UPLOAD_ERROR);
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
