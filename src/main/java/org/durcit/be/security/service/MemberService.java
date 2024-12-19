package org.durcit.be.security.service;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.repository.MemberRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.dto.MemberDetails;
import org.durcit.be.security.util.ProfileImageUtil;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.durcit.be.system.exception.ExceptionMessage.MEMBER_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }


    public Member getById(Long id) {
        return findById(id).stream()
                .filter(Member::isVerified)
                .findFirst().orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));
    }

    public boolean hasRole(Long memberId, String[] roles) {
        return memberRepository.findById(memberId)
                .filter(Member::isVerified)
                .map(member -> {
                    for (String role : roles) {
                        if (member.getRole().equals(role)) {
                            return true;
                        }
                    }
                    return false;
                }).orElse(false);
    }


    public boolean isPresentById(Long id) {
        return findById(id).filter(Member::isVerified).isPresent();
    }


    public MemberDetails loadMemberDetails(Long id) {
        return MemberDetails.from(getById(id));
    }


    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        MemberDetails memberDetails = MemberDetailsFactory.createMemberDetails(registrationId, oAuth2User);

        Optional<Member> findMember = memberRepository.findByEmail(memberDetails.getEmail());
        Member member = findMember.orElseGet(
                () -> {
                    Member savedMember = Member.builder()
                            .email(memberDetails.getEmail())
                            .provider(registrationId)
                            .username(memberDetails.getName())
                            .nickname(generateUniqueNickname())
                            .isVerified(true)
                            .profileImage(ProfileImageUtil.generateRandomProfileImage(memberDetails.getName()))
                            .build();
                    return memberRepository.save(savedMember);
                }
        );

        if (member.getProvider().equals(registrationId)) {
            return memberDetails.setId(member.getId()).setRole(member.getRole());
        } else {
            throw new RuntimeException();
        }
    }

    private String generateUniqueNickname() {
        String nickname;
        do {
            nickname = NickNameFactory.createNickname();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }


    public Member getByNickname(String targetNickname) {
        return memberRepository.findByNickname(targetNickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));
    }
}
