package org.durcit.be.security.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dto.MemberProfileResponse;
import org.durcit.be.security.dto.NicknameRequest;
import org.durcit.be.security.service.ProfileService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseData<MemberProfileResponse>> getMemberProfile() {
        MemberProfileResponse currentMemberProfile = profileService.getCurrentMemberProfile();
        return ResponseData.toResponseEntity(ResponseCode.GET_USER_PROFILE_SUCCESS, currentMemberProfile);
    }




    // 메서드 기능: 입력 닉네임을 받아서 변경하는 기능이다.  
    // 예외: 같은 닉네임이 있으면 예외를 던진다.
    // 반환: 저장한 엔티티를 MemberProfileResponse 로 변환하여 반환한다.
    @PutMapping("/profile/nickname-update")
    public ResponseEntity<ResponseData<MemberProfileResponse>> updateNickname(NicknameRequest nicknameRequest) {
        MemberProfileResponse updatedNicknameMemberProfile = profileService.updateNickName(nicknameRequest);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_NICKNAME_SUCCESS, updatedNicknameMemberProfile);
    }





}
