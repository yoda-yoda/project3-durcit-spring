package org.durcit.be.upload.service;

import org.durcit.be.upload.dto.ProfileImageRequest;

public interface ProfileUploadService {
    public String uploadProfileReturnUrl(ProfileImageRequest profileImageRequest);
}
