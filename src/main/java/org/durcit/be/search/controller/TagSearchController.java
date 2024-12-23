package org.durcit.be.search.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.TagSearchRequest;
import org.durcit.be.search.service.TagSearchService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/tag-search")
public class TagSearchController {


    private final TagSearchService tagSearchService;




    @PostMapping("/post")
    // 메서드 기능: 유저가 검색한 태그를 검색한다. 그리고 태그와 Post가 존재하고 둘다 delete false 인 경우만 찾아서, 응답 List로 변환 후 반환한다.
    // 반환: 해당하는 List<PostCardResponse> 를 반환하고, 없으면 빈 리스트를 반환한다.
    // 빈 List 반환 경우1: 검색한 Tag가 DB에 아예 없으면 빈 List를 반환한다.
    // 빈 List 반환 경우2: 찾은 Tag들이 테이블에서 전부 delete true 상태면 빈 List를 반환한다.
    // 빈 List 반환 경우3: Tag가 있고 delete false 이긴하지만, 해당 Post 자체가 delete true면 빈 List를 반환한다.
    public ResponseEntity<ResponseData<List<PostCardResponse>>> getAllTagSearchWithNoneDelete(TagSearchRequest tagSearchRequest) {

        List<PostCardResponse> postCardResponsesList = tagSearchService.getAllPostCardResponsesWithNoneDeletedPostsTag(tagSearchRequest);

        return ResponseData.toResponseEntity(ResponseCode.GET_TAG_SEARCH_SUCCESS, postCardResponsesList);
    }


    


}
