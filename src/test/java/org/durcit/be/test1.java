package org.durcit.be;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.postsTag.domain.Posts;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.repository.PostsRepository;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.postsTag.service.PostsTagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@Rollback(false)
@SpringBootTest
public class test1 {

    @Autowired
    private PostsTagService postsTagService;
    @Autowired
    private PostsTagRepository postsTagRepository;
    @Autowired
    private PostsRepository postsRepository;

    @Test
    @DisplayName("임시로 만든 Posts 엔티티로 디비에 잘 저장되는지 테스트")
    void posts_save_test() throws Exception {
        Posts posts1 = new Posts();

        posts1.setTitle("title1");
        posts1.setContent("content1");
        posts1.setViews(1L);

        Posts posts2 = new Posts();

        posts2.setTitle("title2");
        posts2.setContent("content2");
        posts2.setViews(1L);

        Posts posts3 = new Posts();

        posts3.setTitle("title3");
        posts3.setContent("content3");
        posts3.setViews(1L);

        postsRepository.save(posts1);
        postsRepository.save(posts2);
        postsRepository.save(posts3);

        log.info("posts1.getPostsTagList() = {}", posts1.getPostsTagList());



    }



    @Test
    @DisplayName("posts 와 postsTag의 save 등등 임시 테스트")
    void posts_tag_save_test() throws Exception {

        Posts posts1 = new Posts();

        posts1.setTitle("title1");
        posts1.setContent("content1");
        posts1.setViews(1L);

        postsRepository.save(posts1);


        Optional<Posts> byId = postsRepository.findById(1L);
        Posts posts = byId.get();

        assertThat(posts).isEqualTo(posts1);


        PostsTag tag1 = PostsTag.builder()
                .contents("태그1")
                .posts(posts1)
                .build();

        postsTagRepository.save(tag1);

        log.info("posts.getPostsTagList() = {}", posts.getPostsTagList());
        // null 나오는게 맞는건지.

        

    }

}
