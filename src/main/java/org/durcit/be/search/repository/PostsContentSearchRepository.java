package org.durcit.be.search.repository;

import org.durcit.be.post.domain.Post;
import org.durcit.be.search.domain.PostsContentSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsContentSearchRepository extends JpaRepository<PostsContentSearch,Long> {

    @Query("SELECT p FROM Post as p WHERE p.content LIKE %:content% AND p.deleted = false")
    public List<Post> findPostsByContentAndNoneDeleted(@Param("content") String content);


}
