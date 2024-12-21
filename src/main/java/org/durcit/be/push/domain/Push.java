package org.durcit.be.push.domain;

import jakarta.persistence.*;
import lombok.*;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Push {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_id")
    private long id;

    private String memberId;

    @Enumerated(EnumType.STRING)
    private PushType pushType;

    private String content;

    private Long postId;

    @Setter
    private boolean confirmed;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Push(String memberId, PushType pushType, String content, Long postId) {
        this.memberId = memberId;
        this.pushType = pushType;
        this.content = content;
        this.postId = postId;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.confirmed = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
