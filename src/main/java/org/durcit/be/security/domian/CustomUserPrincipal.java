package org.durcit.be.security.domian;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserPrincipal extends User {
    private final Long memberId;

    public CustomUserPrincipal() {
        super("", "", List.of());
        this.memberId = null;
    }

    public CustomUserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities, Long memberId) {
        super(username, password, authorities);
        this.memberId = memberId;
    }
}