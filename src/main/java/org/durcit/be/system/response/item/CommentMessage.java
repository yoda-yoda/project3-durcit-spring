package org.durcit.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMessage {
    public static final String CREATE_COMMENT_SUCCESS = "SUCCESS - 댓글 작성 성공";
    public static final String GET_COMMENT_SUCCESS = "SUCCESS - 댓글 조회 성공";
    public static final String UPDATE_COMMENT_SUCCESS = "SUCCESS - 댓글 수정 성공";
    public static final String DELETE_COMMENT_SUCCESS = "SUCCESS - 댓글 삭제 성공";
}
