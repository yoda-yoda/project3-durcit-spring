package org.durcit.be.comment.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

// domain/Entity

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // Post 추가 후 주석제거
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "POST_ID")
//    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "DIVISION", nullable = false)
    private boolean division;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    private LocalDateTime updatedAt;

    @Builder
    public Comment(Long commentId, String content, boolean division) {
        this.commentId = commentId;
        this.content = content;
        this.division = division;

    }

}
