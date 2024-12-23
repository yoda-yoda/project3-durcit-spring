package org.durcit.be.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.security.domian.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFollowResponse {

    private Long memberId;
    private String username;
    private String nickname;
    private String email;
    private String profileImage;

    public static MemberFollowResponse from(Member member) {
        MemberFollowResponse response = new MemberFollowResponse();
        response.memberId = member.getId();
        response.username = member.getUsername();
        response.nickname = member.getNickname();
        response.profileImage = member.getProfileImage();
        response.email = member.getEmail();
        return response;
    }

}
