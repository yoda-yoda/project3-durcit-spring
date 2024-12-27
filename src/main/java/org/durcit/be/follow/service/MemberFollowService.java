package org.durcit.be.follow.service;

import org.durcit.be.follow.domain.MemberFollow;
import org.durcit.be.follow.dto.MemberFollowResponse;

import java.util.List;
import java.util.Optional;

public interface MemberFollowService {

    public void toggleFollow(Long followeeId);
    public List<MemberFollowResponse> getFollowers(Long memberId);
    public List<MemberFollowResponse> getFollowees(Long memberId);
    public boolean isFollowing(Long followeeId);
}
