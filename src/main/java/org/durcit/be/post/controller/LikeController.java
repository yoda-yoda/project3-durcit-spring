package org.durcit.be.post.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.service.LikeService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{postId}/count")
    public ResponseEntity<ResponseData<Long>> getLikeCount(@PathVariable Long postId) {
        long likeCount = likeService.getLikeCount(postId);
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_LIKES_SUCCESS, likeCount);
    }

    @PostMapping("/{postId}/toggle")
    public ResponseEntity<ResponseData<Boolean>> toggleLike(@PathVariable Long postId) {
        boolean currentStatus = likeService.toggleLike(postId);
        return ResponseData.toResponseEntity(ResponseCode.TOGGLE_LIKE_SUCCESS, currentStatus);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseData<List<PostCardResponse>>> getLikedPostsByMember(@PathVariable Long memberId) {
        List<PostCardResponse> likedPosts = likeService.getLikedPostsByMember(memberId);
        return ResponseData.toResponseEntity(ResponseCode.GET_POST_LIKES_SUCCESS, likedPosts);
    }

}
