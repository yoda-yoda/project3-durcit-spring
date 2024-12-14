package org.durcit.be.post.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.PostRegisterRequest;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.dto.PostUpdateRequest;
import org.durcit.be.post.service.PostService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ResponseData<List<PostResponse>>> getPosts() {
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postService.getAllPosts());
    }

    @PostMapping("/members")
    public ResponseEntity<ResponseData<PostResponse>> createPost(@RequestBody PostRegisterRequest postRegisterRequest) {
        PostResponse postResponse = postService.createPost(postRegisterRequest);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POST_SUCCESS, postResponse);
    }

    @PutMapping("/members/{postId}")
    public ResponseEntity<ResponseData> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.updatePost(postId, postUpdateRequest);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_POST_SUCCESS);
    }

    @DeleteMapping("/members/{postId}")
    public ResponseEntity<ResponseData> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_POST_SUCCESS);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseData<PostResponse>> getPostWithInCreaseViews(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPostWithViewIncrement(postId);
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postResponse);
    }



}
