package org.durcit.be.log.repository;

import org.durcit.be.log.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
