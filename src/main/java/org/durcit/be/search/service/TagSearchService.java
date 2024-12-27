package org.durcit.be.search.service;

import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.TagSearchRequest;

import java.util.List;

public interface TagSearchService {



    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeletedPostsTag(TagSearchRequest tagSearchRequest);
    public List<Post> getAllPostsWithNoneDeletedPostsTag(TagSearchRequest tagSearchRequest);


}
