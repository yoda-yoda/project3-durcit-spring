package org.durcit.be.security.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dto.BioRequest;
import org.durcit.be.security.dto.MemberProfileResponse;
import org.durcit.be.security.dto.NicknameRequest;
import org.durcit.be.security.service.ProfileService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/profile/nickname-updates")
    public ResponseEntity<ResponseData<MemberProfileResponse>> updateNickname(NicknameRequest nicknameRequest) {
        MemberProfileResponse updatedNicknameMemberProfile = profileService.updateNickName(nicknameRequest);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_NICKNAME_SUCCESS, updatedNicknameMemberProfile);
    }

    @GetMapping("/profile/nickname-checks")
    public ResponseEntity<ResponseData<Boolean>> getNicknameChecks(@RequestParam NicknameRequest nicknameRequest) {
        boolean result = profileService.isDuplicateNickname(nicknameRequest);
        return ResponseData.toResponseEntity(ResponseCode.CHECK_DUPLICATE_NICKNAME_SUCCESS, result);
    }



    // 메서드 기능: 입력을 받아 자기소개를 변경, 저장하는 기능이다.
    // 예외: 해당 멤버가 아니면 내부 메서드에서 예외를 던진다.
    // 반환: 저장한 엔티티를 MemberProfileResponse 로 변환하여 반환한다.
    @PutMapping("/profile/bio-updates")
    public ResponseEntity<ResponseData<MemberProfileResponse>> updateBio(BioRequest bioRequest) {

        MemberProfileResponse memberProfileResponse = profileService.updateBio(bioRequest);

        return ResponseData.toResponseEntity(ResponseCode.UPDATE_BIO_SUCCESS, memberProfileResponse);

    }




}
