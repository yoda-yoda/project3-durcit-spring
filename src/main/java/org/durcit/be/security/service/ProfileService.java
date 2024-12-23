package org.durcit.be.security.service;

import org.durcit.be.security.dto.MemberProfileResponse;
import org.durcit.be.security.dto.NicknameRequest;

public interface ProfileService {


    public MemberProfileResponse getCurrentMemberProfile();
    public MemberProfileResponse updateNickName(NicknameRequest nicknameRequest);


}
