package org.durcit.be.chat.repository;

import org.durcit.be.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserIdAndOpponentId(Long userId, Long opponentId);

    @Query("SELECT c FROM ChatRoom c WHERE (c.userId = :memberId AND c.opponentId = :targetMemberId) OR (c.userId = :targetMemberId AND c.opponentId = :memberId)")
    Optional<ChatRoom> findByMemberIds(Long memberId, Long targetMemberId);

    List<ChatRoom> findByUserId(Long userId);
    List<ChatRoom> findByOpponentId(Long opponentId);


    @Query("SELECT c FROM ChatRoom c WHERE c.userId = :memberId OR c.opponentId = :memberId")
    List<ChatRoom> findByMemberId(Long memberId);
}
