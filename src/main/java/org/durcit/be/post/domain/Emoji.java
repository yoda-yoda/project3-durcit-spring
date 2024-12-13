package org.durcit.be.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Emoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    @Setter
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    @Column(nullable = false, length = 15)
    private String emoji;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Emoji(Post post, Member member, String emoji) {
        this.post = post;
        this.member = member;
        this.emoji = emoji;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
