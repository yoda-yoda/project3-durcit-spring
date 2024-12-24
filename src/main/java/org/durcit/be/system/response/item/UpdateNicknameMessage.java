package org.durcit.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateNicknameMessage {

    public static final String UPDATE_NICKNAME_SUCCESS = "SUCCESS - 닉네임 변경 성공";
    public static final String CHECK_DUPLICATE_NICKNAME_SUCCESS = "SUCCESS - 닉네임 중복 체크 성공";

}
