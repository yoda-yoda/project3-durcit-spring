package org.durcit.be.comment.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.post.domain.Post;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;

    @Setter
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<CommentMention> mentionList;

    @Setter
    private boolean deleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Comment(String content, Member author, Comment parent, List<CommentMention> mentionList, Post post) {
        this.content = content;
        this.author = author;
        this.parent = parent;
        this.post = post;
        this.mentionList = mentionList;
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
