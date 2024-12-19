package org.durcit.be.postsTag.dto;

import lombok.*;
import org.durcit.be.post.domain.Post;
import org.durcit.be.postsTag.domain.PostsTag;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
public class PostsTagResponse {

    private Long id;
    private String contents;
    private boolean deleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PostsTagResponse(Long id, String contents, boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.contents = contents;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostsTagResponse fromEntity(PostsTag postsTag) {
        return PostsTagResponse.builder()
                .id(postsTag.getId())
                .contents(postsTag.getContents()) // null이 할당될수있는듯.
                .deleted(postsTag.isDeleted())
                .createdAt(postsTag.getCreatedAt())
                .updatedAt(postsTag.getUpdatedAt())
                .build();
    }



}
