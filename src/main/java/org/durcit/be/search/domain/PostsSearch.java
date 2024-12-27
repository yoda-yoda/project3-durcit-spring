package org.durcit.be.search.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Setter;

@Entity
@Setter
public class PostsSearch { // 만들어두긴했는데 안필요한것같다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;







}
