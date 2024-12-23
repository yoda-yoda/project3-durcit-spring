package org.durcit.be.admin.service;

import org.durcit.be.admin.dto.AdminLogResponse;

import java.util.List;

public interface AdminLogService {

    public List<AdminLogResponse> getAllLogs();

}
