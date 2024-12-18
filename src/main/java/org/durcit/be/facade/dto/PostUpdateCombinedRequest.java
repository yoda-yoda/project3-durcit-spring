package org.durcit.be.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.post.dto.PostUpdateRequest;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.upload.dto.UploadUpdateRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateCombinedRequest {
    private PostUpdateRequest postUpdateRequest;
    private List<PostsTagRegisterRequest> postsTagRegisterRequests;
    private UploadUpdateRequest uploadUpdateRequest;
}
