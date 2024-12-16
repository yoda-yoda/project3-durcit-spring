package org.durcit.be.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.post.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRegisterRequest {

    private String title;
    private String content;

    public static Post toEntity(PostRegisterRequest request) {
        return Post.builder()
                .views(0L)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }


}
