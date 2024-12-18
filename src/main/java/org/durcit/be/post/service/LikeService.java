package org.durcit.be.post.service;

public interface LikeService {

    public long getLikeCount(Long postId);
    public boolean toggleLike(Long postId);

}
