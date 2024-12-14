package org.durcit.be.post.repository;

import org.durcit.be.post.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    long countByPostId(Long postId);
    Like findByPostIdAndMemberId(Long postId, Long memberId);

}
