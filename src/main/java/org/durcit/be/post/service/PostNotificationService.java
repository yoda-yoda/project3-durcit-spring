package org.durcit.be.post.service;

import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.follow.dto.TagFollowMembersResponse;
import org.durcit.be.post.dto.PostResponse;

import java.util.List;

public interface PostNotificationService {
    public void notifyFollowers(PostResponse post, List<MemberFollowResponse> followers);
    public void notifyTagFollowers(List<TagFollowMembersResponse> tagFollowers);
}
