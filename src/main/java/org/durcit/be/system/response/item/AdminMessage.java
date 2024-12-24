package org.durcit.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminMessage {

    public static String GET_ADMIN_LOG_SUCCESS = "SUCCESS - 로그 조회 성공";
    public static String RECOVER_POST_SUCCESS = "SUCCESS - 게시물 복구 성공";
    public static String UPDATE_ROLE_SUCCESS = "SUCCESS - 역할 수정 성공";
    public static String HARD_DELETE_POST_AND_POSTS_TAG_SUCCESS = "SUCCESS - 해당 게시물과 태그 영구 삭제 성공";

}
