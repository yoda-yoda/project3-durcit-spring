package org.durcit.be.admin.repository;
import org.durcit.be.admin.domain.Admin;
import org.durcit.be.log.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long>  {

    @Query("SELECT l FROM Log as l")
    public List<Log> findAllLogs();

}
