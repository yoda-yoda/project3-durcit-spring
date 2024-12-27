package org.durcit.be.push.repository;

import org.durcit.be.push.domain.Push;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushRepository extends JpaRepository<Push, Long> {
    List<Push> findAllByMemberIdOrderByCreatedAtDesc(String memberId);
}
