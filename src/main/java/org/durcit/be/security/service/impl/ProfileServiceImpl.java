package org.durcit.be.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.dto.MemberProfileResponse;
import org.durcit.be.security.repository.MemberRepository;
import org.durcit.be.security.service.ProfileService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.durcit.be.system.exception.ExceptionMessage.MEMBER_NOT_FOUND_ERROR;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final MemberRepository memberRepository;

    public MemberProfileResponse getCurrentMemberProfile() {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));

        return MemberProfileResponse.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .isVerified(member.isVerified())
                .role(member.getRole())
                .provider(member.getProvider())
                .build();
    }

}
