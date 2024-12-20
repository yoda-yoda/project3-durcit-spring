package org.durcit.be.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowNotificationMessage {

    private Long followeeId;
    private Long followerId;
    private String followerNickname;
    private String message;

}
