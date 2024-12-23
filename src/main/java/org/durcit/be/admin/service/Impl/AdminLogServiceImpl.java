package org.durcit.be.admin.service.Impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.admin.dto.AdminLogResponse;
import org.durcit.be.admin.repository.AdminLogRepository;
import org.durcit.be.admin.service.AdminLogService;
import org.durcit.be.log.domain.Log;
import org.durcit.be.system.exception.adminLog.AdminLogNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.durcit.be.system.exception.ExceptionMessage.ADMIN_LOG_NOT_FOUND_ERROR;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminLogServiceImpl implements AdminLogService {


    private final AdminLogRepository adminLogRepository;


    public List<AdminLogResponse> getAllLogs() {

        List<Log> findAllLogs = adminLogRepository.findAllLogs();

        if (findAllLogs.isEmpty()) {
            throw new AdminLogNotFoundException(ADMIN_LOG_NOT_FOUND_ERROR);
        }

        List<AdminLogResponse> adminLogResponses = new ArrayList<>();

        for (Log log : findAllLogs) {
            adminLogResponses.add(AdminLogResponse.fromEntity(log));
        }

        return adminLogResponses;
    }




}
