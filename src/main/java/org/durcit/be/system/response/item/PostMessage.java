package org.durcit.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMessage {
    public static final String GET_POST_SUCCESS = "SUCCESS - 게시물 조회 성공";
    public static final String CREATE_POST_SUCCESS = "SUCCESS - 게시물 생성 성공";
    public static final String UPDATE_POST_SUCCESS = "SUCCESS - 게시물 수정 성공";
    public static final String DELETE_POST_SUCCESS = "SUCCESS - 게시물 삭제 성공";
}
