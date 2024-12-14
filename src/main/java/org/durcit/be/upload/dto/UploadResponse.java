package org.durcit.be.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadResponse {

    private String url;
    private String originalFilename;

    @Builder
    public UploadResponse(String url, String originalFilename) {
        this.url = url;
        this.originalFilename = originalFilename;
    }
}
