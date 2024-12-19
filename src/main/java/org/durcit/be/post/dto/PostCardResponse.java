package org.durcit.be.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.post.domain.Post;
import org.durcit.be.system.util.TimeAgoUtil;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class PostCardResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long views;
    private Long likeCount;
    private Long commentCount;
    private String userThumbnail;
    private boolean hasImage;
    private String postThumbnail;
    private String createdAt;

    @Builder
    public PostCardResponse(Long id, String title, String content, String author, Long views, Long likeCount, Long commentCount, String userThumbnail, boolean hasImage, String postThumbnail, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.views = views;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.userThumbnail = userThumbnail;
        this.hasImage = hasImage;
        this.postThumbnail = postThumbnail;
        this.createdAt = createdAt;
    }

    public static PostCardResponse fromEntity(Post post) {
        String thumbnailUrl = null;
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            thumbnailUrl = post.getImages().iterator().next().getUrl();
        }

        return PostCardResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getUsername())
                .views(post.getViews())
                .likeCount((long) post.getLikes().size())
                .hasImage(post.getImages() != null && !post.getImages().isEmpty())
                .userThumbnail(post.getMember().getProfileImage())
                .postThumbnail(thumbnailUrl)
                .createdAt(TimeAgoUtil.formatElapsedTime(post.getUpdatedAt()))
                .build();
    }


}
