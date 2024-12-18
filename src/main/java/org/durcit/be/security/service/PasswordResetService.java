package org.durcit.be.security.service;

public interface PasswordResetService {

    public void sendVerificationCode();
    public boolean verifyCode(String email, String code);
}
