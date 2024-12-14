package org.durcit.be.upload.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.durcit.be.upload.dto.UploadRequest;
import org.durcit.be.upload.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/members/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/files")
    public ResponseEntity<ResponseData> upload(@RequestParam Long postId, @RequestParam List<MultipartFile> files) {
        UploadRequest uploadRequest = UploadRequest.builder()
                .postId(postId)
                .files(files)
                .build();

        uploadService.upload(uploadRequest);
        return ResponseData.toResponseEntity(ResponseCode.UPLOAD_FILES_SUCCESS);
    }

}
