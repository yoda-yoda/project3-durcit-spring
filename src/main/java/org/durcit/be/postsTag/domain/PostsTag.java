package org.durcit.be.postsTag.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne // 현재 Posts는 포스트태그 엔티티를 만들기위해서 임의로 만든 클래스임.
    private Posts posts;



    // 빌더패턴
    @Builder
    public PostsTag(String contents, Posts posts) {
        this.contents = contents;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.posts = posts;
    }






}
