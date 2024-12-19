package org.durcit.be.follow.repository;

import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.security.domian.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagFollowRepository extends JpaRepository<TagFollow, Long> {


    @Query("SELECT m FROM Member as m WHERE m.id = :memberId")
    public Member findMemberByMemberId(@Param("memberId") Long memberId);
    
}
