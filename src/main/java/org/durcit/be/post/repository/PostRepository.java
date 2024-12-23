package org.durcit.be.post.repository;

import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :postId")
    void incrementViews(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId")
    List<Post> findByMemberId(@Param("memberId") Long memberId);

    List<Post> findByTitleContaining(String query);

    @Query("SELECT p FROM Post p JOIN p.postsTagList t WHERE t.contents IN :tags")
    Page<Post> findPostsByTags(@Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.postsTagList t WHERE t.contents = :tag")
    Page<Post> findPostsByTag(@Param("tag") String tag, Pageable pageable);
}
