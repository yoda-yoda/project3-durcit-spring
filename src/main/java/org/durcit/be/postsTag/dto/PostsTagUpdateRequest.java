package org.durcit.be.postsTag.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.durcit.be.postsTag.domain.Posts;
import org.durcit.be.postsTag.domain.PostsTag;

@Getter
@Setter
public class PostsTagUpdateRequest {

    @NotNull
    private String contents;

    public static PostsTag toEntity(PostsTagUpdateRequest request){ // 일단 유빈님처럼 업데이트 빌더도 따라만들어봄.
        return PostsTag.builder()
                .contents(request.getContents())
                .build();
    }
}
