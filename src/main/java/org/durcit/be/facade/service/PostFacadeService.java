package org.durcit.be.facade.service;

import org.durcit.be.facade.dto.PostCombinedResponse;
import org.durcit.be.facade.dto.PostRegisterCombinedRequest;
import org.durcit.be.facade.dto.PostUpdateCombinedRequest;

public interface PostFacadeService {
    public void registerPost(PostRegisterCombinedRequest request);
    public PostCombinedResponse getPostById(Long postId);
    public void updatePosts(PostUpdateCombinedRequest request, Long postId);
    public void deletePosts(Long postId);
}
