package org.durcit.be.search.repository;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
@Slf4j
class PostsSearchRepositoryTest {

    @Autowired
    PostsSearchRepository postsSearchRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("PostsSearchRepository 에 정의한 findPostsByTitleAndNoneDeleted 메서드 테스트")
    void findPostsByTitleAndNoneDeleted_method_test() throws Exception {
    		// given
//        Post post1 = Post.builder()
//                .title("제목1")
//                .build();
//        postRepository.save(post1);
//
//        Optional<Post> byId = postRepository.findById(post1.getId());
//        Post post = byId.get();
//        post.setDeleted(true);
//        postRepository.save(post);
//
//        Post post2 = Post.builder()
//                .title("제목2")
//                .build();
//        postRepository.save(post2);

        List<Post> list = postsSearchRepository.findPostsByTitleAndNoneDeleted("제목1");
        assertThat(list.isEmpty()).isEqualTo(true);
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트.

        // when

    	  // then
    }

}