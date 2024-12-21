package org.durcit.be.system.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    // auth
    public static final String DUPLICATE_EMAIL_ERROR = "이미 사용중인 이메일입니다. 새로운 이메일을 입력해주세요.";
    public static final String NOT_VALID_TOKEN_ERROR = "유효하지 않은 토큰 입니다.";
    public static final String INVALID_USERNAME_PASSWORD_ERROR = "이메일 또는 비밀번호가 잘못되었습니다.";
    public static final String INVALID_PASSWORD_ERROR = "비밀번호가 맞지 않습니다.";
    public static final String INVALID_CHK_PASSWORD_NEW_PASSWORD_ERROR = "신규 비밀번호와 확인 비밀번호가 서로 다릅니다.";
    public static final String EMAIL_NOT_VERIFIED_ERROR = "이메일이 인증되지 않은 회원입니다.";


    public static final String MEMBER_NOT_FOUND_ERROR = "멤버를 찾을 수 없습니다.";
    public static final String INVALID_USER_ERROR = "유효하지 않은 멤버입니다.";
    public static final String UNAUTHORIZED_ACCESS_ERROR = "유효하지 않은 접근입니다. 해당 멤버에게는 권한이 없습니다.";
    public static final String NO_AUTHENTICATION_IN_SECURITY_CONTEXT_ERROR = "인증되지 않은 사용자 접근 입니다.";

    // post
    public static final String POST_NOT_FOUND_ERROR = "해당하는 포스트를 찾을 수 없습니다.";

    // upload
    public static final String S3_UPLOAD_ERROR = "S3 스토리지 업로드 중 문제가 발생하였습니다.";
    public static final String IMAGE_NOT_FOUND_ERROR = "해당 이미지를 찾을 수 없습니다.";
    public static final String FILE_SIZE_EXCEED_MAXIMUM_LIMIT_ERROR = "파일 용량 제한을 벗어났습니다.";

    // postsTag
    public static final String OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR = "findById로 찾아온 PostsTag의 Optional 객체가 비어있습니다.";
    public static final String OPTIONAL_EMPTY_POSTS_TAG_BY_CONTENTS_ERROR = "요청(검색) Dto의 contents를 가진 엔티티가 태그 테이블에 존재하지 않습니다.";
    public static final String OPTIONAL_EMPTY_POSTS_TAG_BY_FIND_ALL_ERROR = "PostsTag 리포지터리의 FindAll로 찾은 Optional 객체가 비어있습니다.";

    // chat
    public static final String INVALID_CHAT_ROOM_ID_ERROR = "유효하지 않은 채팅방 아이디 입니다.";
    public static final String INVALID_CHAT_ROOM_CREATION_ERROR = "유효하지 않은 채팅방 개설입니다.";

    // push
    public static final String PUSH_CANNOT_FOUND_ERROR = "해당 푸시알람을 찾을 수 없습니다.";
}
