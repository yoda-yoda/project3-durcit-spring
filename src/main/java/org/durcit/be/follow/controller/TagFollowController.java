package org.durcit.be.follow.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.follow.dto.TagFollowResponse;
import org.durcit.be.follow.service.TagFollowService;
import org.durcit.be.follow.service.impl.TagFollowServiceImpl;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/tag-follow/members/{memberId}")
public class TagFollowController {


    private final TagFollowServiceImpl tagFollowServiceImpl;







    @GetMapping
    // 해당 서비스 메서드 기능: 멤버 pk로 해당 멤버가 가진 태그를 획득하고, 응답 Dto를 리스트에 담아 반환한다.
    // 반환: 가진 태그가 전부 delete true이거나, 아예 태그 테이블이 비어있다면 빈 리스트를 반환한다.
    public ResponseEntity<ResponseData<List<TagFollowResponse>>> getTagFollows(@PathVariable("memberId") Long memberId) {

        List<TagFollowResponse> tagFollowsResponses = tagFollowServiceImpl.getTagFollowsResponses(memberId);

        return ResponseData.toResponseEntity(ResponseCode.GET_TAG_FOLLOW_SUCCESS, tagFollowsResponses);
    }


    @DeleteMapping
    // 해당 서비스 메서드 기능: 해당 유저가 기존에 갖고 있던 태그를 전부 소프트딜리트 처리 한다.
    // 예외: 기존에 아무 태그를 안갖고 있었거나, 이미 전부가 delete 처리된 상태였다면 예외 처리한다.
    public ResponseEntity<ResponseData> deleteTagFollows(@PathVariable("memberId") Long memberId) {

        tagFollowServiceImpl.deleteNoneDeletedAllTagFollows(memberId);

        return ResponseData.toResponseEntity(ResponseCode.DELETE_TAG_FOLLOW_SUCCESS);
    }









}
