package org.durcit.be.admin.repository;
import org.durcit.be.admin.domain.Admin;
import org.durcit.be.log.domain.Log;
import org.durcit.be.security.domian.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long>  {

    @Query("SELECT l FROM Log as l")
    public List<Log> findAllLogs();

    @Query("SELECT m FROM Member as m WHERE m.id = :memberId")
    public Member findMemberById(Long memberId);

}
