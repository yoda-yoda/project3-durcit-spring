package org.durcit.be.search.repository;

import org.durcit.be.post.domain.Post;
import org.durcit.be.search.domain.PostsSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PostsSearchRepository extends JpaRepository<PostsSearch, Long> {

    @Query("SELECT p FROM Post as p WHERE p.title = :title AND p.deleted = false")
    public List<Post> findPostsByTitleAndNoneDeleted(@Param("title") String title);

}
