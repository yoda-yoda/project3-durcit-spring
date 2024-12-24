package org.durcit.be.follow.repository;

import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.security.domian.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagFollowRepository extends JpaRepository<TagFollow, Long> {


    @Query("SELECT m FROM Member as m WHERE m.id = :memberId")
    public Member findMemberByMemberId(@Param("memberId") Long memberId);


    @Query("SELECT t.member FROM TagFollow as t WHERE t.tag = :tag")
    public List<Member> findMemberByTag(@Param("tags") String tag);

    Optional<TagFollow> findByMemberAndTag(@Param("member") Member member, @Param("tag") String tag);


    List<String> findFollowedTagsByMemberId(@Param("memberId") Long memberId);

    boolean existsByTagAndMemberId(String contents, Long memberId);

    List<TagFollow> findTagsByMemberId(Long memberId);
}
