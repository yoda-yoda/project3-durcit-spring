package org.durcit.be.search.repository;

import org.durcit.be.post.domain.Post;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.search.domain.TagSearch;
import org.durcit.be.search.dto.TagSearchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagSearchRepository extends JpaRepository<TagSearch, Long> {

    // 매개변수의 contents 와 일치하는 태그를 가졌고 동시에 delete false인 PostsTag 들을 찾는 jpql이다.
    @Query("SELECT pt FROM PostsTag as pt WHERE pt.contents = :contents AND pt.deleted = false")
    List<PostsTag> findPostsTagByContentsWithNoneDeleted(@Param("contents") String contents);


    // 만들어놓긴했는데 안써도 되는듯하다.
    // 매개변수의 id 를 가진 게시글이면서 동시에 delete false인 Post 들을 찾는 jpql이다.
    @Query("SELECT p FROM Post as p WHERE p.id = :id AND p.deleted = false")
    List<Post> findPostsByIdWithNoneDeleted(@Param("id") Long id);









}
