package org.durcit.be.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.*;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ResponseData<List<PostResponse>>> getPosts() {
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postService.getAllPosts());
    }

    @GetMapping("/posts/pages")
    public ResponseEntity<ResponseData<Page<PostCardResponse>>> getPostsByPage(@Valid PostPageRequest postPageRequest) {
        PageRequest pageRequest = PageRequest.of(postPageRequest.getPage(), postPageRequest.getSize());
        Page<PostCardResponse> postsByPage = postService.getPostsByPage(pageRequest, postPageRequest.getCategory());
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postsByPage);
    }

    //@PostMapping("/members")
    public ResponseEntity<ResponseData<PostResponse>> createPost(@RequestBody PostRegisterRequest postRegisterRequest) {
        PostResponse postResponse = postService.createPost(postRegisterRequest);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POST_SUCCESS, postResponse);
    }

    //@PutMapping("/members/{postId}")
    public ResponseEntity<ResponseData> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.updatePost(postId, postUpdateRequest);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_POST_SUCCESS);
    }

    //@DeleteMapping("/members/{postId}")
    public ResponseEntity<ResponseData> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_POST_SUCCESS);
    }

    //@GetMapping("/{postId}")
    public ResponseEntity<ResponseData<PostResponse>> getPostWithInCreaseViews(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPostWithViewIncrement(postId);
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postResponse);
    }

    @GetMapping("/members/my-posts")
    public ResponseEntity<ResponseData<List<PostCardResponse>>> getMyPosts() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        List<PostCardResponse> myPosts = postService.getMyPosts(currentMemberId);
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, myPosts);
    }

}
