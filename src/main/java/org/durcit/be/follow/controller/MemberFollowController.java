package org.durcit.be.follow.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.dto.FollowRequest;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.follow.service.MemberFollowService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members/follows")
@RequiredArgsConstructor
public class MemberFollowController {

    private final MemberFollowService memberFollowService;

    @PostMapping("/toggle")
    public ResponseEntity<ResponseData> toggleFollow(@RequestBody FollowRequest followRequest) {
        memberFollowService.toggleFollow(followRequest.getFolloweeId());
        return ResponseData.toResponseEntity(ResponseCode.TOGGLE_MEMBER_FOLLOW_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseData<Boolean>> isFollowing(@RequestParam Long followeeId) {
        boolean isFollowing = memberFollowService.isFollowing(followeeId);
        return ResponseData.toResponseEntity(ResponseCode.GET_MEMBER_FOLLOWER_SUCCESS, isFollowing);
    }

    @GetMapping("/followers")
    public ResponseEntity<ResponseData<List<MemberFollowResponse>>> getFollowers(@RequestParam Long followeeId) {
        List<MemberFollowResponse> followers = memberFollowService.getFollowers(followeeId);
        return ResponseData.toResponseEntity(ResponseCode.GET_MEMBER_FOLLOWER_SUCCESS, followers);
    }

    @GetMapping("/followees")
    public ResponseEntity<ResponseData<List<MemberFollowResponse>>> getFollowees(@RequestParam Long followerId) {
        List<MemberFollowResponse> followees = memberFollowService.getFollowees(followerId);
        return ResponseData.toResponseEntity(ResponseCode.GET_MEMBER_FOLLOWEE_SUCCESS, followees);
    }




}
