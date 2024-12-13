package org.durcit.be.security.domian;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String provider;
    private String nickname;

    @Setter
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    private String role = "MEMBER";

    @Column(nullable = false)
    @Setter
    private boolean isVerified;

    private LocalDateTime signedAt = LocalDateTime.now();

    @Setter
    private LocalDateTime updatedAt;

    @Builder
    public Member(Long id, String username, String provider, String email, String nickname, String password, boolean isVerified) {
        this.id = id;
        this.username = username;
        this.provider = provider;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.isVerified = isVerified;
    }
}
