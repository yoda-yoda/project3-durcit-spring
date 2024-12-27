package org.durcit.be.facade.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.facade.dto.PostCombinedResponse;
import org.durcit.be.facade.dto.PostRegisterCombinedRequest;
import org.durcit.be.facade.service.impl.PostFacadeServiceImpl;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostFacadeController {

    private final PostFacadeServiceImpl postFacadeService;

    @PostMapping("/members/posts")
    public ResponseEntity<ResponseData<Long>> registerPost(@RequestBody PostRegisterCombinedRequest request) {
        Long postId = postFacadeService.registerPost(request);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POST_SUCCESS, postId);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<PostCombinedResponse>> getPosts(@PathVariable Long postId, @RequestBody Long memberId) {
        PostCombinedResponse postById = postFacadeService.getPostById(postId, memberId);
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postById);
    }


}
