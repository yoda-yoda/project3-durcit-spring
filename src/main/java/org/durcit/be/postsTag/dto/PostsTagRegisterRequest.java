package org.durcit.be.postsTag.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.durcit.be.post.domain.Post;
import org.durcit.be.postsTag.domain.PostsTag;

@Data
@Getter
@Setter
public class PostsTagRegisterRequest {

    @NotNull
    private String contents;

    public static PostsTag toEntity(PostsTagRegisterRequest request, Post post) {
        return PostsTag.builder()
                .contents(request.getContents())
                .post(post)
                .build();
    }


}
