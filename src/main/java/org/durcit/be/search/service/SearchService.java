package org.durcit.be.search.service;

import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.SearchRequest;

import java.util.List;
import org.durcit.be.search.dto.SearchResultResponse;

public interface SearchService {
    public List<SearchResultResponse> search(String query);

    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(SearchRequest searchRequest);

}
