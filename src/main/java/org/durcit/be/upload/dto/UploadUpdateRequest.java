package org.durcit.be.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadUpdateRequest {

    private Long postId;

    private List<Long> imageIdsToDelete;

    private List<MultipartFile> newFiles;

}
