package org.durcit.be.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.domian.VerificationInfo;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.service.PasswordResetService;
import org.durcit.be.security.util.SecurityUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final MemberService memberService;
    private final JavaMailSender mailSender;
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

    public boolean verifyCode(String email, String code) {
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
