package org.durcit.be.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.post.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static Post toEntity(PostUpdateRequest request) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
