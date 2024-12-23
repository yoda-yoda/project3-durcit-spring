package org.durcit.be.security.service;

import org.durcit.be.security.dto.PasswordResetRequest;

public interface PasswordResetService {

    public void sendVerificationCode();
    public boolean verifyCode(String code);
    public void changePassword(PasswordResetRequest request);
}
