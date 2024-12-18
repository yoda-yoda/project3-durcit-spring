package org.durcit.be.facade.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ResponseData> registerPost(@RequestBody PostRegisterCombinedRequest request) {
        postFacadeService.registerPost(request);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POST_SUCCESS);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseData> getPosts(@PathVariable Long postId) {

        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS);
    }


}
