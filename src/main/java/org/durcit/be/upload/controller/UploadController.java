package org.durcit.be.upload.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.durcit.be.upload.dto.UploadRequest;
import org.durcit.be.upload.dto.UploadUpdateRequest;
import org.durcit.be.upload.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/members/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/files")
    public ResponseEntity<ResponseData> upload(@RequestBody UploadRequest request) {
        uploadService.upload(request);
        return ResponseData.toResponseEntity(ResponseCode.UPLOAD_FILES_SUCCESS);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseData> updateImages(@RequestBody UploadUpdateRequest request) {
        uploadService.updateImages(request);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_FILES_SUCCESS);
    }



}
