package org.durcit.be.post.service;

import org.durcit.be.post.dto.PostResponse;

import java.util.List;

public interface PostService {

    public List<PostResponse> getAllPosts();
    public PostResponse getPostById(Long postId);

}
