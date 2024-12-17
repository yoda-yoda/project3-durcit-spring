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
    private Post post; // PostsTagResponse 라는 응답 Dto 인데 이 필드가 필요한가 지금 의문이다.

    @Builder
    public PostsTagResponse(Long id, String contents, boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, Post post) {
        this.id = id;
        this.contents = contents;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.post = post;
    }

    public static PostsTagResponse fromEntity(PostsTag postsTag) {
        return PostsTagResponse.builder()
                .id(postsTag.getId())
                .contents(postsTag.getContents()) // null이 할당될수있는듯.
                .deleted(postsTag.isDeleted())
                .createdAt(postsTag.getCreatedAt())
                .updatedAt(postsTag.getUpdatedAt())
                .post(postsTag.getPost())
                .build();
    }



}
