package org.durcit.be.upload.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.durcit.be.post.domain.Post;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "images_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Post post;

    @Column(columnDefinition = "TEXT")
    private String url;

    private String originalFilename;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Images(Long id, Post post, String url, String originalFilename) {
        this.id = id;
        this.post = post;
        this.url = url;
        this.originalFilename = originalFilename;
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
