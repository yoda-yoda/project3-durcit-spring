package org.durcit.be.search.service;

import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.PostsContentSearchRequest;

import java.util.List;

public interface PostsContentSearchService {


    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(PostsContentSearchRequest postsContentSearchRequest);


}
