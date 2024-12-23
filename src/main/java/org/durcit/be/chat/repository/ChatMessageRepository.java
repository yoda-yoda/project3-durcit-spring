package org.durcit.be.chat.repository;

import org.durcit.be.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdOrderByCreatedAt(Long chatRoomId);

    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.id = :roomId ORDER BY m.createdAt ASC")
    List<ChatMessage> findByChatRoomId(Long roomId);
}
