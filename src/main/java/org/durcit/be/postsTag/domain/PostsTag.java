package org.durcit.be.postsTag.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected
// jpa 테이블 생성시 엔티티의 기본생성자를 사용하는데
// 이때 상속을 받아서 사용해요 그래서 반드시 기본생성자가 public or protected 여야합니다
public class PostsTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @Setter
    private boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Post post;

    // 빌더패턴
    @Builder
    public PostsTag(String contents, Post post) {
        this.contents = contents;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.post = post;
    }








}
