package org.durcit.be.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileResponse {

    private String email;
    private String username;
    private String nickname;
    private String profileImage;
    private boolean isVerified;
    private String role;
    private String provider;

}
