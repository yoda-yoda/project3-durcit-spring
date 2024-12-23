package org.durcit.be.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.security.domian.Member;
import org.durcit.be.upload.domain.Images;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Member member;

    private String title;

    private String content;

    @Setter
    private Long views;

    @Setter
    private boolean deleted;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Like> likes;

    @Setter
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Images> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostsTag> postsTagList;

    @Builder
    public Post(Long id, Member member, String title, String content, Long views) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.views = views;

    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
