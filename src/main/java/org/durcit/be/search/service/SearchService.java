package org.durcit.be.search.service;

import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.SearchRequest;

import java.util.List;

public interface SearchService {

    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(SearchRequest searchRequest);

}
