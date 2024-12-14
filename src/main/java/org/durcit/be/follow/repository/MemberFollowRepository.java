package org.durcit.be.follow.repository;

import org.durcit.be.follow.domain.MemberFollow;
import org.durcit.be.security.domian.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberFollowRepository extends JpaRepository<MemberFollow, Long> {
    Optional<MemberFollow> findByFollowerAndFollowee(Member follower, Member followee);

    @Query("SELECT mf.follower FROM MemberFollow mf WHERE mf.followee = :followee AND mf.status = 'FOLLOWED'")
    List<Member> findFollowersByFollowee(@Param("followee") Member followee);

    @Query("SELECT mf.followee FROM MemberFollow mf WHERE mf.follower = :follower AND mf.status = 'FOLLOWED'")
    List<Member> findFolloweesByFollower(@Param("follower") Member follower);
}
