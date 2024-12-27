package org.durcit.be.search.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.search.service.SearchService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.durcit.be.search.dto.SearchResultResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/search")
    public ResponseEntity<ResponseData<List<SearchResultResponse>>> search(@RequestParam("query") String query) {
        List<SearchResultResponse> results = searchService.search(query);
        return ResponseData.toResponseEntity(ResponseCode.SEARCH_SUCCESS, results);
    }
}