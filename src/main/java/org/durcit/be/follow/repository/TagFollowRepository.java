package org.durcit.be.follow.repository;

import org.durcit.be.follow.domain.TagFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagFollowRepository extends JpaRepository<TagFollow, Long> {
    
}
