package org.durcit.be.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.domian.VerificationInfo;
import org.durcit.be.security.dto.PasswordResetRequest;
import org.durcit.be.security.repository.MemberRepository;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.service.PasswordResetService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.exception.auth.InvalidChkPasswordWithNewPassword;
import org.durcit.be.system.exception.auth.InvalidPasswordException;
import org.durcit.be.system.exception.auth.InvalidUserException;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.durcit.be.system.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final MemberService memberService;
    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, VerificationInfo> verificationStore = new ConcurrentHashMap<>();
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes

    public void sendVerificationCode() {
        String email = memberService.getById(SecurityUtil.getCurrentMemberId()).getEmail();
        String code = generateVerificationCode();
        long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME_MS;

        verificationStore.put(email, new VerificationInfo(code, expirationTime));

        sendEmail(email, code);
    }

    public boolean verifyCode(String code) {
        String email = memberService.getById(SecurityUtil.getCurrentMemberId()).getEmail();
        VerificationInfo info = verificationStore.get(email);

        if (info == null || System.currentTimeMillis() > info.getExpirationTime()) {
            verificationStore.remove(email);
            return false;
        }

        if (info.getCode().equals(code)) {
            verificationStore.remove(email);
            return true;
        }

        return false;
    }


    public void changePassword(PasswordResetRequest request) {
        String email = memberService.getById(SecurityUtil.getCurrentMemberId()).getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));

        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new InvalidPasswordException(INVALID_PASSWORD_ERROR);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidChkPasswordWithNewPassword(INVALID_CHK_PASSWORD_NEW_PASSWORD_ERROR);
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Verification Code");
        message.setText("Your verification code is: " + code + "\nThis code will expire in 5 minutes.");
        mailSender.send(message);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
}
