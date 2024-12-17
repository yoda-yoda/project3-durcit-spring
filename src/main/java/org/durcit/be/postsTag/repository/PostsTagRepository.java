package org.durcit.be.postsTag.repository;

import org.durcit.be.post.domain.Post;
import org.durcit.be.postsTag.domain.PostsTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsTagRepository extends JpaRepository<PostsTag, Long> {

    List<PostsTag> findByContents(String contents);

    // 241217 기준으로, 해당 메서드 쓰는곳에서 옵셔널 처리해야할듯. 설계중.
    @Query("SELECT p FROM Post as p WHERE p.id = :postId")  // postId 를 통해 jpql로 List<Post> 를 받아온다.
    public Post findPostByPostId(@Param("postId") Long postId);

}
