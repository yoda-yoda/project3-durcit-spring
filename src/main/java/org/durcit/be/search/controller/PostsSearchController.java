package org.durcit.be.search.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.PostsSearchRequest;
import org.durcit.be.search.service.PostsSearchService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts-search")
public class PostsSearchController {


    private final PostsSearchService postsSearchService;



    @PostMapping("/post")
    // 메서드 기능: 유저가 검색창에 게시글 제목을 검색하면, 해당 제목을 가진 Post를 모두 조회한다.
    // 예외 X: 즉 해당하는 제목이 없거나, delete가 true면 예외를 던지지않고, 최종적으로 빈 List를 반환하도록 한다.
    // 반환: 있다면 응답 Dto로 변환해 List로 담아 최종 반환한다.
    public ResponseEntity<ResponseData<List<PostCardResponse>>> getAllPostsSearchWithNoneDelete(PostsSearchRequest postsSearchRequest) {

        List<PostCardResponse> postCardResponsesList = postsSearchService.getAllPostCardResponsesWithNoneDeleted(postsSearchRequest);

        return ResponseData.toResponseEntity(ResponseCode.GET_POSTS_SEARCH_SUCCESS, postCardResponsesList);
    }






}
