package org.durcit.be.postsTag.repository;

import org.durcit.be.postsTag.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {

}
