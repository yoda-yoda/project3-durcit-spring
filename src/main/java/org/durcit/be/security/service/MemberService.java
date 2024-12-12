package org.durcit.be.security.service;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dao.MemberRepository;
import org.durcit.be.security.dao.VerificationTokenRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.domian.VerificationToken;
import org.durcit.be.security.dto.MemberDetails;
import org.durcit.be.security.dto.RegisterRequest;
import org.durcit.be.system.exception.auth.DuplicateEmailException;
import org.durcit.be.system.exception.auth.NotValidTokenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.durcit.be.system.exception.ExceptionMessage.DUPLICATE_EMAIL_ERROR;
import static org.durcit.be.system.exception.ExceptionMessage.NOT_VALID_TOKEN_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }


    public Member getById(Long id) {
        return findById(id).orElseThrow(
                () -> new NoSuchElementException("Member not found")
        );
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



}
