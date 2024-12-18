package org.durcit.be.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// jpa 테이블 생성시에 엔티티 기본생성자 사용하는데, 상속받아서 사용할 때 기본 생성자가 반드시 PROTECTED로


public class PostComment {

    // coment_id, posts_id, member_id, contents, division, created_at, updated_at, comment_id2
    // 게시물이아니라 -> 댓글의 대댓글 구현
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private Comment comment;
}
