package org.durcit.be.security.dto;

import lombok.*;
import lombok.experimental.Accessors;
import org.durcit.be.security.domian.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetails implements OAuth2User {

    @Setter
    private Long id;

    private String name;
    private String email;

    @Setter
    private String role;

    private Map<String, Object> attributes;

    @Builder
    public MemberDetails(String name, String email, Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        return name;
    }

    public static MemberDetails from(Member member) {
        MemberDetails memberDetails = new MemberDetails();
        memberDetails.id = member.getId();
        memberDetails.email = member.getEmail();
        memberDetails.role = member.getRole();
        memberDetails.name = member.getUsername();
        return memberDetails;
    }
}
