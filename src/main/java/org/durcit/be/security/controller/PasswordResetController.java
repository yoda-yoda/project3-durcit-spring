package org.durcit.be.security.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.service.PasswordResetService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset() {
        passwordResetService.sendVerificationCode();
        return ResponseEntity.ok("Verification code sent to email.");
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseData> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean verified = passwordResetService.verifyCode(email, code);
        if (verified) {
            return ResponseData.toResponseEntity(ResponseCode.VERIFY_EMAIL_SUCCESS);
        } else {
            return ResponseData.toResponseEntity(ResponseCode.VERIFY_EMAIL_FAIL);
        }
    }

}
