package org.durcit.be.follow.domain;

import jakarta.persistence.*;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

@Entity
public class TagFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;



}
