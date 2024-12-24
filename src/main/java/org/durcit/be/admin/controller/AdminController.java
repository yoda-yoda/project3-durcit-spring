package org.durcit.be.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.admin.dto.AdminLogResponse;
import org.durcit.be.admin.service.AdminService;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.service.PostService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("api/admins")
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;


    // 메서드 기능: Log(api_log) 테이블을 전부 조회한다.
    // 반환: 각각의 엔티티를 응답 Dto로 변환후 List로 반환한다.
    @GetMapping("/log")
    public ResponseEntity<ResponseData<List<AdminLogResponse>>> getAdminLogs() {

        List<AdminLogResponse> allLogs = adminService.getAllLogs();

        return ResponseData.toResponseEntity(ResponseCode.GET_ADMIN_LOG_SUCCESS, allLogs);

    }



    // 메서드 기능: PostId를 받아 해당 Post와 거기 담긴 Tag를 전부 delete false 설정한다.
    // 예외: 해당하는 Post가 없으면 예외를 던진다.
    // 반환: 작업이 끝난 Post를 PostCard 타입으로 변환후 반환한다.
    // 수정할것: 댓글 부분을 살리는 로직을 추가해야한다.
    @PutMapping("/recover/{postId}")
    public ResponseEntity<ResponseData> recoverPost(@PathVariable Long postId) {

        adminService.recoverPostAndPostsTag(postId);

        return ResponseData.toResponseEntity(ResponseCode.RECOVER_POST_SUCCESS);

    }







}
