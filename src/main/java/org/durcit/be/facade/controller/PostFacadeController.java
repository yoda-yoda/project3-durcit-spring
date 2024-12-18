package org.durcit.be.facade.controller;

import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.ResponseDate;
import org.durcit.be.facade.dto.PostRegisterCombinedRequest;
import org.durcit.be.facade.service.PostFacadeService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostFacadeController {

    private final PostFacadeService postFacadeService;

    @PostMapping
    public ResponseEntity<ResponseData> registerPost(@RequestBody PostRegisterCombinedRequest request) {
        postFacadeService.registerPost(request);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POST_SUCCESS);
    }


}
