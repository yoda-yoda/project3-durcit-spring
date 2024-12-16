package org.durcit.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowMessage {

    public static final String TOGGLE_MEMBER_FOLLOW_SUCCESS = "SUCCESS - 멤버 팔로우 성공";
    public static final String GET_MEMBER_FOLLOWER_SUCCESS = "SUCCESS - 멤버 팔로워 조회 성공";
    public static final String GET_MEMBER_FOLLOWEE_SUCCESS = "SUCCESS - 멤버 팔로위 조회 성공";

}
