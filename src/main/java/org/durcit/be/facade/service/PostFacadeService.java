package org.durcit.be.facade.service;

import org.durcit.be.facade.dto.PostCombinedResponse;
import org.durcit.be.facade.dto.PostRegisterCombinedRequest;
import org.durcit.be.facade.dto.PostUpdateCombinedRequest;

public interface PostFacadeService {
    public Long registerPost(PostRegisterCombinedRequest request);
    public PostCombinedResponse getPostById(Long postId, Long memberId);
    public void updatePosts(PostUpdateCombinedRequest request, Long postId);
    public void deletePosts(Long postId);
}
