package org.durcit.be.post.service;

import org.durcit.be.post.dto.PostCardResponse;

import java.util.List;

public interface LikeService {

    public long getLikeCount(Long postId);
    public boolean toggleLike(Long postId);
    public List<PostCardResponse> getLikedPostsByMember(Long memberId);

}
