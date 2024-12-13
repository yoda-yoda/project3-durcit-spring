package org.durcit.be.postsTag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


// 지금 이 Posts 엔티티를 만든 목적:
// => PostsTag 엔티티를 만들기위해서 대충만 구현함. 왜냐면 PostsTag 엔티티 안에 Posts 엔티티가 있어야하기 때문이다.
// 이 엔티티의 특징:
// => ERD 설계도처럼 만든게아님. 즉 외래키인 member_id는 생략된 상태다.

@Entity
@Getter
@Setter
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postsId;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long views;

    @OneToMany(mappedBy = "posts")
    private List<PostsTag> postsTagList;


}
