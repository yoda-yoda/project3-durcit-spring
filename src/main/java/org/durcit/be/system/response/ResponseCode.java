package org.durcit.be.system.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.durcit.be.system.response.item.FollowMessage;
import org.durcit.be.system.response.item.Message;
import org.durcit.be.system.response.item.PostMessage;
import org.durcit.be.system.response.item.Status;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    CREATED_USER(Status.CREATED, Message.CREATED_USER),
    NOT_FOUND_USER(Status.NOT_FOUND, Message.NOT_FOUND_USER),
    DUPLICATE_USER(Status.BAD_REQUEST, Message.DUPLICATE_USER),

    // 기타 성공 응답
    READ_IS_LOGIN(Status.OK, Message.READ_IS_LOGIN),
    LOGIN_SUCCESS(Status.OK, Message.LOGIN_SUCCESS),
    GET_LOGIN(Status.NO_CONTENT, Message.GET_LOGIN),
    UPDATE_PASSWORD(Status.NO_CONTENT, Message.UPDATE_PASSWORD),
    HEALTHY_SUCCESS(Status.OK, Message.HEALTHY_SUCCESS),
    REISSUE_SUCCESS(Status.OK, Message.REISSUE_SUCCESS),

    // 기타 실패 응답
    INTERNAL_SERVER_ERROR(Status.INTERNAL_SERVER_ERROR, Message.INTERNAL_SERVER_ERROR),
    anonymousUser_ERROR(Status.INTERNAL_SERVER_ERROR, Message.anonymousUser_ERROR),
    UNAUTHORIZED_ERROR(Status.UNAUTHORIZED, Message.UNAUTHORIZED),
    FORBIDDEN_ERROR(Status.FORBIDDEN, Message.FORBIDDEN),
    LOGIN_FAIL(Status.BAD_REQUEST, Message.LOGIN_FAIL),
    TOKEN_EXPIRED(Status.UNAUTHORIZED, Message.TOKEN_EXPIRED),
    TOKEN_ERROR(Status.UNAUTHORIZED, Message.TOKEN_ERROR),

    // post
    GET_POST_SUCCESS(Status.OK, PostMessage.GET_POST_SUCCESS),
    CREATE_POST_SUCCESS(Status.CREATED, PostMessage.CREATE_POST_SUCCESS),
    UPDATE_POST_SUCCESS(Status.NO_CONTENT, PostMessage.UPDATE_POST_SUCCESS),
    DELETE_POST_SUCCESS(Status.NO_CONTENT, PostMessage.DELETE_POST_SUCCESS),

    TOGGLE_EMOJI_SUCCESS(Status.NO_CONTENT, PostMessage.TOGGLE_EMOJI_SUCCESS),

    GET_POST_LIKES_SUCCESS(Status.OK, PostMessage.GET_POST_LIKES_SUCCESS),
    TOGGLE_LIKE_SUCCESS(Status.OK, PostMessage.TOGGLE_LIKE_SUCCESS),

    // upload
    UPLOAD_FILES_SUCCESS(Status.CREATED, PostMessage.UPLOAD_FILES_SUCCESS),
    UPDATE_FILES_SUCCESS(Status.NO_CONTENT, PostMessage.UPDATE_FILES_SUCCESS),

    // member follow
    TOGGLE_MEMBER_FOLLOW_SUCCESS(Status.NO_CONTENT, FollowMessage.TOGGLE_MEMBER_FOLLOW_SUCCESS),
    GET_MEMBER_FOLLOWER_SUCCESS(Status.OK, FollowMessage.GET_MEMBER_FOLLOWER_SUCCESS),
    GET_MEMBER_FOLLOWEE_SUCCESS(Status.OK, FollowMessage.GET_MEMBER_FOLLOWEE_SUCCESS),

    // push
    GET_PUSHS_SUCCESS(Status.OK, PostMessage.GET_PUSHS_SUCCESS),
    ;

    private int httpStatus;
    private String message;

}
