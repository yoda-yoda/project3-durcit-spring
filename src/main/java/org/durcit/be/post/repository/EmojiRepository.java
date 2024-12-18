package org.durcit.be.post.repository;

import org.durcit.be.post.domain.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

    Emoji findByPostIdAndMemberIdAndEmoji(Long postId, Long memberId, String emoji);

    @Query("SELECT e.emoji AS emoji, COUNT(e) AS count FROM Emoji e WHERE e.post.id = :postId GROUP BY e.emoji")
    Map<String, Integer> aggregateEmojisByPostId(Long postId);

}
