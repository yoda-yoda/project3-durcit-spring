package org.durcit.be.security.domian;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.follow.domain.TagFollow;

import java.time.LocalDateTime;
import java.util.List;

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

    @Setter
    @Column(columnDefinition = "TEXT")
    private String profileImage;

    private LocalDateTime signedAt;

    @Setter
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member")
    private List<TagFollow> tagFollowList;


    @Builder
    public Member(Long id, String username, String provider, String email, String nickname, String password, boolean isVerified, String profileImage) {
        this.id = id;
        this.username = username;
        this.provider = provider;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.isVerified = isVerified;
        this.profileImage = profileImage;
    }

    @PrePersist
    protected void onCreate() {
        this.signedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
