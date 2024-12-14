package org.durcit.be.upload.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.annotation.PostConstruct;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.service.PostService;
import org.durcit.be.system.exception.upload.ImageNotFoundException;
import org.durcit.be.upload.domain.Images;
import org.durcit.be.upload.dto.UploadRequest;
import org.durcit.be.upload.dto.UploadUpdateRequest;
import org.durcit.be.upload.repository.ImagesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class S3ServiceImplTest {
    
    private AmazonS3 amazonS3;
    private PostService postService;
    private ImagesRepository imagesRepository;
    private S3ServiceImpl s3Service;
    
    @BeforeEach
    void setUp() {
        amazonS3 = mock(AmazonS3.class);
        postService = mock(PostService.class);
        imagesRepository = mock(ImagesRepository.class);
        s3Service = new S3ServiceImpl(amazonS3, postService, imagesRepository);
        ReflectionTestUtils.setField(s3Service, "bucketName", "mock-bucket");
    }
    
    @Test
    @DisplayName("S3에 파일 업로드 및 세부 내용 데이터베이스에 저장 -> 성공")
    void upload_shouldUploadFilesToS3AndSaveToDatabase() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.png",
                "image/jpeg",
                "test content".getBytes()
        );

        UploadRequest request = UploadRequest.builder()
                .postId(1L)
                .files(List.of(file))
                .build();

        Post post = Post.builder().build();
        when(postService.getById(1L)).thenReturn(post);
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://mock-bucket.s3.amazonaws.com/image.png"));


        // when
        s3Service.upload(request);

        // then
        ArgumentCaptor<String> bucketNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(amazonS3, times(1)).putObject(
                bucketNameCaptor.capture(),
                anyString(),
                any(ByteArrayInputStream.class),
                any(ObjectMetadata.class)
        );

        System.out.println("Captured bucketName: " + bucketNameCaptor.getValue());
        assertEquals("mock-bucket", bucketNameCaptor.getValue());

        ArgumentCaptor<Images> captor = ArgumentCaptor.forClass(Images.class);
        verify(imagesRepository, times(1)).save(captor.capture());

        Images savedImage = captor.getValue();
        assertThat(savedImage.getPost()).isNotNull();
        assertThat(savedImage.getUrl()).contains("mock-bucket");
    }

    @Test
    @DisplayName("S3에서 파일 삭제 및 데이터베이스에서 제거 -> 성공")
    void deleteImages_shouldRemoveFilesFromS3AndDatabase() {
        // given
        Images image = Images.builder()
                .id(1L)
                .url("https://mock-bucket.s3.amazonaws.com/image.png")
                .build();

        when(imagesRepository.findById(1L)).thenReturn(Optional.of(image));

        // when
        s3Service.deleteImages(List.of(1L));

        // then
        verify(amazonS3, times(1)).deleteObject(eq("mock-bucket"), eq("image.png"));
        verify(imagesRepository, times(1)).delete(image);
    }

    @Test
    @DisplayName("삭제할 이미지가 존재하지 않을 경우 예외 발생")
    void deleteImages_shouldThrowExceptionIfImageNotFound() {
        // given
        when(imagesRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ImageNotFoundException.class, () -> s3Service.deleteImages(List.of(1L)));
        verifyNoInteractions(amazonS3);
    }

    @Test
    @DisplayName("이미지 업데이트 -> 기존 이미지 삭제 및 새로운 이미지 업로드")
    void updateImages_shouldDeleteOldImagesAndUploadNewOnes() throws Exception {
        // given
        Images existingImage = Images.builder()
                .id(1L)
                .url("https://mock-bucket.s3.amazonaws.com/old-image.png")
                .build();

        MockMultipartFile newFile = new MockMultipartFile(
                "file",
                "new-image.png",
                "image/png",
                "new content".getBytes()
        );

        UploadUpdateRequest request = UploadUpdateRequest.builder()
                .postId(1L)
                .imageIdsToDelete(List.of(1L))
                .newFiles(List.of(newFile))
                .build();

        when(imagesRepository.findById(1L)).thenReturn(Optional.of(existingImage));
        Post post = Post.builder().build();
        when(postService.getById(1L)).thenReturn(post);
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://mock-bucket.s3.amazonaws.com/new-image.png"));

        // when
        s3Service.updateImages(request);

        // then
        verify(amazonS3, times(1)).deleteObject(eq("mock-bucket"), eq("old-image.png"));
        verify(imagesRepository, times(1)).delete(existingImage);

        ArgumentCaptor<String> bucketNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(amazonS3, times(1)).putObject(
                bucketNameCaptor.capture(),
                anyString(),
                any(ByteArrayInputStream.class),
                any(ObjectMetadata.class)
        );

        assertEquals("mock-bucket", bucketNameCaptor.getValue());

        ArgumentCaptor<Images> captor = ArgumentCaptor.forClass(Images.class);
        verify(imagesRepository, times(1)).save(captor.capture());

        Images savedImage = captor.getValue();
        assertThat(savedImage.getUrl()).contains("mock-bucket.s3.amazonaws.com/new-image.png");
        assertThat(savedImage.getOriginalFilename()).isEqualTo("new-image.png");
    }

    @Test
    @DisplayName("여러 파일 업로드 -> S3 및 데이터베이스에 저장")
    void upload_shouldUploadMultipleFiles() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "image1.png",
                "image/png",
                "file content 1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "image2.png",
                "image/png",
                "file content 2".getBytes()
        );

        UploadRequest request = UploadRequest.builder()
                .postId(1L)
                .files(List.of(file1, file2))
                .build();

        Post post = Post.builder().build();
        when(postService.getById(1L)).thenReturn(post);
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenAnswer(invocation -> {
                    String key = ((GeneratePresignedUrlRequest) invocation.getArgument(0)).getKey();
                    return new URL("https://mock-bucket.s3.amazonaws.com/" + key);
                });

        // when
        s3Service.upload(request);

        // then
        ArgumentCaptor<String> bucketNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(amazonS3, times(2)).putObject(
                bucketNameCaptor.capture(),
                anyString(),
                any(ByteArrayInputStream.class),
                any(ObjectMetadata.class)
        );

        assertEquals("mock-bucket", bucketNameCaptor.getValue());

        ArgumentCaptor<Images> captor = ArgumentCaptor.forClass(Images.class);
        verify(imagesRepository, times(2)).save(captor.capture());

        List<Images> savedImages = captor.getAllValues();
        assertThat(savedImages.size()).isEqualTo(2);
        assertThat(savedImages.get(0).getUrl()).contains("image1.png");
        assertThat(savedImages.get(1).getUrl()).contains("image2.png");
    }

}