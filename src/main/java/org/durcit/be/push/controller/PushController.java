package org.durcit.be.push.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.push.dto.PushResponse;
import org.durcit.be.push.service.PushService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/pushs")
@Slf4j
public class PushController {

    private final PushService pushService;

    @GetMapping
    public ResponseEntity<ResponseData<List<PushResponse>>> getPushsByMemberId(String memberId) {
        List<PushResponse> pushsByMemberId = pushService.getPushsByMemberId(memberId);
        return ResponseData.toResponseEntity(ResponseCode.GET_PUSHS_SUCCESS, pushsByMemberId);
    }

    @PutMapping("/{pushId}/confirm")
    public ResponseEntity<ResponseData> confirmPush(@PathVariable Long pushId) {
        log.info("pushId = {}", pushId);
        pushService.confirmPush(pushId);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_PUSH_SUCCESS);
    }


}
