package org.durcit.be.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private Long views;

    @Setter
    private boolean deleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public Post(Member member, String title, String content, Long views) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.views = views;
    }

}
