package org.durcit.be.security.service.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailMessage {
    public static final String EMAIL_SUBJECT = "DURCIT 회원가입 인증 이메일";
    public static final String EMAIL_LINK_MENTION = "아래 링크를 클릭하여 이메일 인증을 완료해주세요 \n";
}
