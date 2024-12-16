package org.durcit.be.security.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dto.KeyPair;
import org.durcit.be.security.dto.LoginRequest;
import org.durcit.be.security.service.AuthService;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<KeyPair> login(@RequestBody LoginRequest loginRequest) {
        KeyPair keyPair = authService.login(loginRequest);
        return ResponseEntity.ok(keyPair);
    }

}
