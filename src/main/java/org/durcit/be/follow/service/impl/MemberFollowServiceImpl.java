package org.durcit.be.follow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.follow.domain.FollowStatus;
import org.durcit.be.follow.domain.MemberFollow;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.follow.repository.MemberFollowRepository;
import org.durcit.be.follow.service.MemberFollowService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberFollowServiceImpl implements MemberFollowService {

    private final MemberFollowRepository memberFollowRepository;
    private final MemberService memberService;

    @Transactional
    public void toggleFollow(Long followeeId) {
        Member follower = memberService.getById(SecurityUtil.getCurrentMemberId());
        Member followee = memberService.getById(followeeId);

        MemberFollow follow = memberFollowRepository
                .findByFollowerAndFollowee(follower, followee)
                .orElseGet(() -> MemberFollow.builder()
                        .follower(follower)
                        .followee(followee)
                        .status(FollowStatus.UNFOLLOWED)
                        .build()
                );

        if (follow.getStatus() == FollowStatus.FOLLOWED) {
            follow.setStatus(FollowStatus.UNFOLLOWED);
        } else {
            follow.setStatus(FollowStatus.FOLLOWED);
        }

        memberFollowRepository.save(follow);
    }

    public List<MemberFollowResponse> getFollowers(Long memberId) {
        Member currentUser = memberService.getById(memberId);
        List<Member> followers = memberFollowRepository.findFollowersByFollowee(currentUser);

        return followers.stream()
                .map(MemberFollowResponse::from)
                .toList();
    }

    public List<MemberFollowResponse> getFollowees(Long memberId) {
        Member currentUser = memberService.getById(memberId);
        List<Member> followees = memberFollowRepository.findFolloweesByFollower(currentUser);

        return followees.stream()
                .map(MemberFollowResponse::from)
                .toList();
    }

}
