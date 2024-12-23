package org.durcit.be.follow.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.follow.dto.TagFollowRegisterRequest;
import org.durcit.be.follow.dto.TagFollowResponse;
import org.durcit.be.follow.service.TagFollowService;
import org.durcit.be.follow.service.impl.TagFollowServiceImpl;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members/tag-follow")
public class TagFollowController {


    private final TagFollowService tagFollowService;



    @PostMapping("/{memberId}/post")
    // 해당 서비스 메서드 기능 => 해당 멤버가 어떤 태그를 최초 저장하는거라면 DB에 저장시키고, 최초 저장이 아니라면 delete 처리를 반대로 바꿔놓는다.
    // 메서드 목적: 이 메서드 하나로 저장, 삭제, 재저장, 재삭제가 가능하도록 했다.
    // 반환: 해당 태그에 대한 응답Dto 1개를 반환한다.
    public ResponseEntity<ResponseData<TagFollowResponse>> createAndDeleteMemberTagFollow(@RequestBody TagFollowRegisterRequest tagFollowRegisterRequest, @PathVariable("memberId") Long memberId ){

        TagFollowResponse tagFollowResponse = tagFollowService.createAndDeleteTagFollowByRegisterRequest(tagFollowRegisterRequest, memberId);

        return ResponseData.toResponseEntity(ResponseCode.CREATE_TAG_FOLLOW_SUCCESS , tagFollowResponse);
    }



    @GetMapping("/{memberId}")
    // 해당 서비스 메서드 기능: 멤버 pk로 해당 멤버가 가진 태그를 획득하고, 응답 Dto를 리스트에 담아 반환한다.
    // 반환: 가진 태그가 전부 delete true이거나, 아예 태그 테이블이 비어있다면 빈 리스트를 반환한다.
    public ResponseEntity<ResponseData<List<TagFollowResponse>>> getMemberTagFollows(@PathVariable("memberId") Long memberId) {

        List<TagFollowResponse> tagFollowsResponses = tagFollowService.getTagFollowsResponses(memberId);

        return ResponseData.toResponseEntity(ResponseCode.GET_TAG_FOLLOW_SUCCESS, tagFollowsResponses);
    }



    @GetMapping
    // 목적: 관리자 페이지를 위한 메서드다.
    // 해당 서비스 메서드 기능: 테이블의 존재하는 모든 데이터들을 응답 Dto 리스트에 담아 반환한다.
    // 예외 처리X: 아예 DB가 비어있으면 예외처리가 아니라, 빈 리스트를 반환하도록 했다.
    // delete 처리: true든 false 든 그냥 다 가져온다.
    public ResponseEntity<ResponseData<List<TagFollowResponse>>> getAllTagFollowsWithDelete() {

        List<TagFollowResponse> tagFollowsResponses = tagFollowService.getAllTagFollowResponses();

        return ResponseData.toResponseEntity(ResponseCode.GET_TAG_FOLLOW_SUCCESS, tagFollowsResponses);
    }




    @DeleteMapping("/{memberId}/delete")
    // 해당 서비스 메서드 기능: 해당 유저가 기존에 갖고 있던 태그를 전부 소프트딜리트 처리 한다.
    // 예외: 기존에 아무 태그를 안갖고 있었거나, 이미 전부가 delete 처리된 상태였다면 예외 처리한다.
    public ResponseEntity<ResponseData> deleteTagFollows(@PathVariable("memberId") Long memberId) {

        tagFollowService.deleteNoneDeletedAllTagFollows(memberId);

        return ResponseData.toResponseEntity(ResponseCode.DELETE_TAG_FOLLOW_SUCCESS);
    }









}
