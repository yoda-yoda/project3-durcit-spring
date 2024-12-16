package org.durcit.be.postsTag.repository;

import org.durcit.be.postsTag.domain.PostsTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsTagRepository extends JpaRepository<PostsTag, Long> {

    List<PostsTag> findByContents(String contents);
}
