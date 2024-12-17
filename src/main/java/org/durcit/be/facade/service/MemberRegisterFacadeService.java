package org.durcit.be.facade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.facade.dto.MemberRegisterCombinedRequest;
import org.durcit.be.security.service.AuthService;
import org.durcit.be.upload.service.ProfileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberRegisterFacadeService {

    private final AuthService authService;
    private final ProfileUploadService profileUploadService;

    public void registerMemberWithProfileImage(MemberRegisterCombinedRequest memberRegisterCombinedRequest) {
        String profileImageUrl = profileUploadService.uploadProfileReturnUrl(memberRegisterCombinedRequest.getProfileImageRequest());
        authService.register(memberRegisterCombinedRequest.getRegisterRequest(), profileImageUrl);
    }

}
