package org.durcit.be.post.dto;

import lombok.*;
import org.durcit.be.post.domain.Post;
import org.durcit.be.system.util.TimeAgoUtil;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long views;
    private String createdAt;

    @Builder
    public PostResponse(Long id, String title, String content, String author, Long views, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.views = views;
        this.createdAt = createdAt;
    }

    public static PostResponse fromEntity(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getUsername())
                .views(post.getViews())
                .createdAt(TimeAgoUtil.formatElapsedTime(post.getUpdatedAt()))
                .build();
    }

}
