package org.durcit.be.upload.service;

import org.durcit.be.upload.dto.UploadRequest;
import org.durcit.be.upload.dto.UploadUpdateRequest;

import java.util.List;

public interface UploadService {

    public void upload(UploadRequest request);
    public void deleteImages(List<Long> imageIds);
    public void updateImages(UploadUpdateRequest request);

}
