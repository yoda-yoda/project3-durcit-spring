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


    @PutMapping("/profile/nickname-update")
    public ResponseEntity<ResponseData<MemberProfileResponse>> updateNickname(NicknameRequest nicknameRequest) {
        MemberProfileResponse updatedNicknameMemberProfile = profileService.updateNickName(nicknameRequest);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_NICKNAME_SUCCESS, updatedNicknameMemberProfile);
    }





}
