package org.durcit.be.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.security.dto.RegisterRequest;
import org.durcit.be.security.dto.TokenRequest;
import org.durcit.be.security.service.AuthService;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.durcit.be.system.response.item.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData> memberRegister(@Valid @RequestBody RegisterRequest request) {
        authService.register(request, null);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_USER);
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseData> verifyEmail(@RequestBody @Valid TokenRequest tokenRequest) {
        log.info("token = {}", tokenRequest.getToken());
        authService.verifyEmail(tokenRequest.getToken());
        return ResponseData.toResponseEntity(ResponseCode.VERIFY_EMAIL_SUCCESS);
    }

}
