package org.durcit.be.search.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PostsContentSearch { // 엔티티가 필요없는것같다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
