package org.durcit.be.search.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TagSearch { // 필요없는 엔티티인것같다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
