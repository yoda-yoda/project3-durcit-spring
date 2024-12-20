package org.durcit.be.follow.service.impl;

import org.durcit.be.follow.domain.FollowStatus;
import org.durcit.be.follow.domain.MemberFollow;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.follow.repository.MemberFollowRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.MockSecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberFollowServiceImplTest {
    @Mock
    private MemberFollowRepository memberFollowRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberFollowServiceImpl memberFollowService;

    private Member follower;
    private Member followee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        follower = Member.builder().id(1L).username("followerUser").build();
        followee = Member.builder().id(2L).username("followeeUser").build();
    }

    @Test
    @DisplayName("팔로우 상태 토글 -> FOLLOWED에서 UNFOLLOWED로 변경")
    void toggleFollow_shouldToggleToUnfollowed() {
        // given
        MemberFollow existingFollow = MemberFollow.builder()
                .follower(follower)
                .followee(followee)
                .status(FollowStatus.FOLLOWED)
                .build();

        when(memberService.getById(follower.getId())).thenReturn(follower);
        when(memberService.getById(followee.getId())).thenReturn(followee);
        when(memberFollowRepository.findByFollowerAndFollowee(follower, followee))
                .thenReturn(Optional.of(existingFollow));

        MockSecurityUtil.mockSecurityContext(1L);

        // when
        memberFollowService.toggleFollow(followee.getId());

        // then
        ArgumentCaptor<MemberFollow> captor = ArgumentCaptor.forClass(MemberFollow.class);
        verify(memberFollowRepository, times(1)).save(captor.capture());

        MemberFollow savedFollow = captor.getValue();
        assertThat(savedFollow.getStatus()).isEqualTo(FollowStatus.UNFOLLOWED);
    }

    @Test
    @DisplayName("팔로우 상태 토글 -> UNFOLLOWED에서 FOLLOWED로 변경")
    void toggleFollow_shouldToggleToFollowed() {
        // given
        MemberFollow existingFollow = MemberFollow.builder()
                .follower(follower)
                .followee(followee)
                .status(FollowStatus.UNFOLLOWED)
                .build();

        when(memberService.getById(follower.getId())).thenReturn(follower);
        when(memberService.getById(followee.getId())).thenReturn(followee);
        when(memberFollowRepository.findByFollowerAndFollowee(follower, followee))
                .thenReturn(Optional.of(existingFollow));

        MockSecurityUtil.mockSecurityContext(1L);

        // when
        memberFollowService.toggleFollow(followee.getId());

        // then
        ArgumentCaptor<MemberFollow> captor = ArgumentCaptor.forClass(MemberFollow.class);
        verify(memberFollowRepository, times(1)).save(captor.capture());

        MemberFollow savedFollow = captor.getValue();
        assertThat(savedFollow.getStatus()).isEqualTo(FollowStatus.FOLLOWED);
    }

    @Test
    @DisplayName("팔로워 목록 조회 -> 성공")
    void getFollowers_shouldReturnFollowerList() {
        // given
        when(memberService.getById(followee.getId())).thenReturn(followee);
        when(memberFollowRepository.findFollowersByFollowee(followee))
                .thenReturn(List.of(follower));

        // when
        List<MemberFollowResponse> followers = memberFollowService.getFollowers(followee.getId());

        // then
        assertThat(followers.size()).isEqualTo(1);
        assertThat(followers.get(0).getMemberId()).isEqualTo(follower.getId());
        assertThat(followers.get(0).getUsername()).isEqualTo(follower.getUsername());
    }

    @Test
    @DisplayName("팔로우 목록 조회 -> 성공")
    void getFollowees_shouldReturnFolloweeList() {
        // given
        when(memberService.getById(follower.getId())).thenReturn(follower);
        when(memberFollowRepository.findFolloweesByFollower(follower.getId()))
                .thenReturn(List.of(followee));

        // when
        List<MemberFollowResponse> followees = memberFollowService.getFollowees(follower.getId());

        // then
        assertThat(followees.size()).isEqualTo(1);
        assertThat(followees.get(0).getMemberId()).isEqualTo(followee.getId());
        assertThat(followees.get(0).getUsername()).isEqualTo(followee.getUsername());
    }
}