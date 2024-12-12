package org.durcit.be.security.service;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.service.item.EmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${custom.email.verification-url}")
    private String verificationUrl;

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(EmailMessage.EMAIL_SUBJECT);
        message.setText(EmailMessage.EMAIL_LINK_MENTION + verificationUrl + token);
        mailSender.send(message);
    }

}
