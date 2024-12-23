package org.durcit.be.search.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Search {   // 엔티티는 필요없는듯.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
