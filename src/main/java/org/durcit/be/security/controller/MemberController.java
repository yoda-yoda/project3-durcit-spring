package org.durcit.be.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dto.RegisterRequest;
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
public class MemberController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData> memberRegister(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_USER);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(Message.VERIFY_EMAIL_SUCCESS);
    }

}
