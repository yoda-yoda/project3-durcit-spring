package org.durcit.be.system.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String DUPLICATE_EMAIL_ERROR = "이미 사용중인 이메일입니다. 새로운 이메일을 입력해주세요.";
    public static final String NOT_VALID_TOKEN_ERROR = "유효하지 않은 토큰 입니다.";
    public static final String INVALID_USERNAME_PASSWORD_ERROR = "이메일 또는 비밀번호가 잘못되었습니다.";
    public static final String EMAIL_NOT_VERIFIED_ERROR = "이메일이 인증되지 않은 회원입니다.";

}
