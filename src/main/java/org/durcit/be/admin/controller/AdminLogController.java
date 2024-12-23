package org.durcit.be.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.admin.dto.AdminLogResponse;
import org.durcit.be.admin.service.AdminLogService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("api/admins")
public class AdminLogController {

    AdminLogService adminLogService;

    @GetMapping("/log")
    public ResponseEntity<ResponseData<List<AdminLogResponse>>> getAdminLogs() {

        List<AdminLogResponse> allLogs = adminLogService.getAllLogs();

        return ResponseData.toResponseEntity(ResponseCode.GET_ADMIN_LOG_SUCCESS, allLogs);

    }


}
