package org.durcit.be.security.service;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dao.MemberRepository;
import org.durcit.be.security.dao.VerificationTokenRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.domian.VerificationToken;
import org.durcit.be.security.dto.KeyPair;
import org.durcit.be.security.dto.LoginRequest;
import org.durcit.be.security.dto.RegisterRequest;
import org.durcit.be.system.exception.auth.DuplicateEmailException;
import org.durcit.be.system.exception.auth.EmailNotVerifiedException;
import org.durcit.be.system.exception.auth.InvalidUsernamePasswordException;
import org.durcit.be.system.exception.auth.NotValidTokenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.durcit.be.system.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void register(RegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException(DUPLICATE_EMAIL_ERROR);
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname().isBlank() ? generateUniqueNickname() : request.getNickname())
                .isVerified(false)
                .build();

        memberRepository.save(member);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, member);
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(member.getEmail(), token);
    }

    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotValidTokenException(NOT_VALID_TOKEN_ERROR));

        Member member = verificationToken.getMember();
        member.setVerified(true);
        memberRepository.save(member);
        tokenRepository.delete(verificationToken);
    }

    private String generateUniqueNickname() {
        String nickname;
        do {
            nickname = NickNameFactory.createNickname();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }

    @Transactional
    public KeyPair login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidUsernamePasswordException(INVALID_USERNAME_PASSWORD_ERROR));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidUsernamePasswordException(INVALID_USERNAME_PASSWORD_ERROR);
        }

        if (!member.isVerified()) {
            throw new EmailNotVerifiedException(EMAIL_NOT_VERIFIED_ERROR);
        }

        return jwtTokenProvider.generateKeyPair(member);
    }

}
