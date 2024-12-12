package org.durcit.be.security.service;

import org.durcit.be.security.dto.MemberDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class MemberDetailsFactory {

    public static MemberDetails createMemberDetails(String provider, OAuth2User oAuth2User) {
        SocialType socialType = SocialType.valueOf(provider.toUpperCase());
        return socialType.createMemberDetails(oAuth2User.getAttributes());
    }

}
