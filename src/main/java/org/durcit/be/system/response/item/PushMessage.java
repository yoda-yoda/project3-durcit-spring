package org.durcit.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PushMessage {

    public static final String UPDATE_PUSH_SUCCESS = "해당 푸시 알림을 확인하였습니다.";
    public static final String GET_PUSHS_SUCCESS = "SUCCESS - 푸시 알람 조회 성공";
}
