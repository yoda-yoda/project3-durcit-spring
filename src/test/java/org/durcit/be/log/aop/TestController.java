package org.durcit.be.log.aop;

import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping("/login")
    public ResponseEntity<ResponseData<String>> login() {
        return ResponseData.toResponseEntity(ResponseCode.LOGIN_SUCCESS, "hello");
    }

    @GetMapping("/hello")
    public ResponseEntity<ResponseData<String>> hello() {
        return ResponseData.toResponseEntity(ResponseCode.LOGIN_SUCCESS, "hello");
    }

}
