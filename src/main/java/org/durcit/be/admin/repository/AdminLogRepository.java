package org.durcit.be.admin.repository;

import org.durcit.be.admin.domain.AdminLog;
import org.durcit.be.log.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long>  {

    @Query("SELECT l FROM Log as l")
    public List<Log> findAllLogs();

}
