package org.durcit.be.system.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    // auth
    public static final String DUPLICATE_EMAIL_ERROR = "이미 사용중인 이메일입니다. 새로운 이메일을 입력해주세요.";
    public static final String NOT_VALID_TOKEN_ERROR = "유효하지 않은 토큰 입니다.";
    public static final String INVALID_USERNAME_PASSWORD_ERROR = "이메일 또는 비밀번호가 잘못되었습니다.";
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
    public static final String EMPTY_POSTS_TAG_LIST_IN_POST_ERROR = "Post 안의 연관관계 필드인 PostsTagList 객체가 비어있습니다. (비어있는 List 입니다.)";
    public static final String NO_POSTS_TAG_IN_LIST_TYPE_ERROR = "내부적 로직상 찾아올수있는 PostsTag 객체가 없어서, 담아둘 공간인 List<PostsTag> 가 빈 리스트 입니다.";

    // tagFollow
    public static final String NO_TAG_FOLLOW_IN_LIST_TYPE_ERROR = "내부적 로직상 찾아올수있는 tagFollow 객체가 없어서, 담아둘 공간인 List<tagFollow> 가 빈 리스트 입니다.";
    public static final String TAG_FOLLOW_NOT_FOUND_ERROR = "해당하는 팔로우 태그를 찾을 수 없습니다.";


}
