package org.durcit.be.comment.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.comment.dto.MentionResponse;
import org.durcit.be.comment.service.MentionService;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MentionController {

    private final MentionService mentionService;

    @GetMapping("/mentions")
    public ResponseEntity<ResponseData<List<MentionResponse>>> getMentionableMembers(@RequestParam String query) {
        List<MentionResponse> mentions = mentionService.getMentionableMembers(query);
        return ResponseData.toResponseEntity(ResponseCode.GET_MENTION_POSSIBLE_MEMBERS_SUCCESS, mentions);
    }


}
