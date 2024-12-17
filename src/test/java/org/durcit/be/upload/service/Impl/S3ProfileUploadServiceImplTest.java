package org.durcit.be.upload.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.durcit.be.system.exception.upload.S3UploadException;
import org.durcit.be.upload.dto.ProfileImageRequest;
import org.durcit.be.upload.util.UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ProfileUploadServiceImplTest {

    private AmazonS3 amazonS3;
    private S3ProfileUploadServiceImpl s3ProfileUploadService;

    @BeforeEach
    void setUp() {
        amazonS3 = mock(AmazonS3.class);
        s3ProfileUploadService = new S3ProfileUploadServiceImpl(amazonS3);

        // S3 설정값 초기화
        ReflectionTestUtils.setField(s3ProfileUploadService, "bucketName", "mock-bucket");
        ReflectionTestUtils.setField(s3ProfileUploadService, "presignedUrlExpirationMinutes", 10);
        ReflectionTestUtils.setField(UploadUtil.class, "maxFileSize", 10 * 1024 * 1024L); // 10MB 설정
    }

    @Test
    @DisplayName("S3에 프로필 이미지 업로드 및 URL 반환 -> 성공")
    void uploadProfileReturnUrl_shouldUploadToS3AndReturnUrl() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile-image.png",
                "image/png",
                "test content".getBytes()
        );

        ProfileImageRequest request = new ProfileImageRequest(file);

        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://mock-bucket.s3.amazonaws.com/profile-image.png"));

        // when
        String result = s3ProfileUploadService.uploadProfileReturnUrl(request);

        // then
        assertThat(result).isEqualTo("https://mock-bucket.s3.amazonaws.com/profile-image.png");

        ArgumentCaptor<String> bucketNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(amazonS3, times(1)).putObject(
                bucketNameCaptor.capture(),
                anyString(),
                any(ByteArrayInputStream.class),
                any(ObjectMetadata.class)
        );

        assertThat(bucketNameCaptor.getValue()).isEqualTo("mock-bucket");
    }

    @Test
    @DisplayName("프로필 이미지가 없는 경우 -> 랜덤 이미지 URL 반환")
    void uploadProfileReturnUrl_shouldReturnRandomImageUrlIfNoImageProvided() {
        // given
        ProfileImageRequest request = new ProfileImageRequest(null);

        // when
        String result = s3ProfileUploadService.uploadProfileReturnUrl(request);

        // then
        assertThat(result).isNull();
        verifyNoInteractions(amazonS3);
    }

    @Test
    @DisplayName("S3 업로드 중 예외 발생 -> S3UploadException 발생")
    void uploadProfileReturnUrl_shouldThrowS3UploadExceptionOnFailure() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile-image.png",
                "image/png",
                "test content".getBytes()
        );

        ProfileImageRequest request = new ProfileImageRequest(file);

        doThrow(new RuntimeException("S3 Error")).when(amazonS3).putObject(
                anyString(),
                anyString(),
                any(ByteArrayInputStream.class),
                any(ObjectMetadata.class)
        );

        // when & then
        assertThrows(S3UploadException.class, () -> s3ProfileUploadService.uploadProfileReturnUrl(request));

        verify(amazonS3, times(1)).putObject(
                anyString(),
                anyString(),
                any(ByteArrayInputStream.class),
                any(ObjectMetadata.class)
        );
    }

}