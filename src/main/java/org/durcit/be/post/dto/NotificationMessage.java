package org.durcit.be.post.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationMessage {
    private Long followerId;
    private Long postId;
    private String message;

    @Builder
    public NotificationMessage(Long followerId, Long postId, String message) {
        this.followerId = followerId;
        this.postId = postId;
        this.message = message;
    }
}
