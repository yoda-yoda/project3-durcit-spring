package org.durcit.be.post.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.service.PostService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getPosts() {
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_SUCCESS, postService.getPostById(id));
    }



}
