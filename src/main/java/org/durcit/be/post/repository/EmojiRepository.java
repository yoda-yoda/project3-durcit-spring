package org.durcit.be.post.repository;

import org.durcit.be.post.domain.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

    @Query("SELECT e FROM Emoji e WHERE e.post.id = :postId AND e.member.id = :memberId AND e.emoji = :emoji")
    Emoji findByPostIdAndMemberIdAndEmoji(@Param("postId") Long postId, @Param("memberId") Long memberId, @Param("emoji") String emoji);

    @Query("SELECT e.emoji AS emoji, COUNT(e.id) AS count FROM Emoji e WHERE e.post.id = :postId GROUP BY e.emoji")
    List<Object[]> aggregateEmojisByPostId(Long postId);

    boolean existsByPostIdAndMemberIdAndEmoji(Long postId, Long currentMemberId, String emoji);

    @Query("SELECT e FROM Emoji e WHERE e.post.id = :postId AND e.member.id = :memberId")
    List<Emoji> findByPostIdAndMemberId(Long postId, Long memberId);

    @Query("SELECT e FROM Emoji e WHERE e.post.id = :postId")
    List<Emoji> findByPostId(Long postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Emoji e WHERE e.post.id = :postId AND e.member.id = :memberId")
    void deleteByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);

}
