package org.durcit.be.search.service;

import java.util.List;
import org.durcit.be.search.dto.SearchResultResponse;

public interface SearchService {
    public List<SearchResultResponse> search(String query);

}
