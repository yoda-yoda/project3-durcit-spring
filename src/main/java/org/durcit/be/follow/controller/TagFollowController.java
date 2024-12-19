package org.durcit.be.follow.controller;

import org.durcit.be.follow.dto.TagFollowResponse;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/tag-follow/members/{memberId}")
public class TagFollowController {


    @GetMapping
    public ResponseEntity<ResponseData<List<TagFollowResponse>>> getTagFollow(@PathVariable("memberId") Long memberId) {

        List<TagFollowResponse> aaa = new ArrayList<>(); // 서비스 만드는중.

        return ResponseData.toResponseEntity(ResponseCode.GET_TAG_FOLLOW_SUCCESS,aaa);
    }



}
