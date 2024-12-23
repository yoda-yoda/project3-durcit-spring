package org.durcit.be.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.post.dto.PostRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRegisterCombinedRequest {

    private PostRegisterRequest postRegisterRequest;
    private List<PostsTagRegisterRequest> postsTagRegisterRequest;

}
