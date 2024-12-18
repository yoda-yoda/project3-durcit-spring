package org.durcit.be.postsTag.repository;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostService;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.service.PostsTagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@Transactional
@Rollback(false)
@SpringBootTest
class PostsTagRepositoryTest {

    // 생성자 주입으로 하려니까 오류가나서 필드주입으로 했다. Junit 테스트는 생성자주입이 잘안먹히나보다.
    @Autowired
    private PostsTagService postsTagService;
    @Autowired
    private PostsTagRepository postsTagRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;


    @Test
    @DisplayName("임시로 만든 Post 엔티티 save 테스트_(PostsTag 엔티티안의 post 필드에 주입해야해서 확인해야함)")
    void Post_save_test() throws Exception {
        // given
        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        // when
        postRepository.save(post1);
        postRepository.save(post2);

        // then
        // 실제 MySQL에서 직접 확인하기.
        // 결과: ok
    }

    @Test
    @DisplayName("임시로 만든 Post 엔티티 findById 테스트")
    void Post_findById_test() throws Exception {
        // given

        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();


        postRepository.save(post1);
        postRepository.save(post2);

        // when
        Optional<Post> byId = postRepository.findById(post2.getId());


        // then
        assertThat(byId.isPresent()).isEqualTo(true);

        Post findPost = byId.get();
        assertThat(findPost.getContent()).isEqualTo(post2.getContent());

    }



    @Test
    @DisplayName("PostsTagRepository save 테스트")
    void posts_tag_repository_save_test() throws Exception {

        // given

        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        PostsTag tag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        // when
        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        // then
        // 실제 MySQL에서 확인
        // 결과 : ok

    }

    @Test
    @DisplayName("PostsTagRepository findById 테스트")
    void posts_tag_repository_findById_test() throws Exception {
        // given
        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        PostsTag tag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        // when
        Optional<PostsTag> byId = postsTagRepository.findById(tag3.getId());

        // then
        assertThat(byId.isPresent()).isEqualTo(true);
        PostsTag postTagFindById = byId.get();
        assertThat(postTagFindById.getContents()).isEqualTo(tag3.getContents());

        // 결과: ok

    }

    @Test
    @DisplayName("PostsTagRepository findByContents 테스트")
    void posts_tag_repository_findByContents_test() throws Exception {
        // given

        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        PostsTag tag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        // when
        List<PostsTag> findByContents = postsTagRepository.findByContents(tag3.getContents());

        // then
        assertThat(ofNullable(findByContents).isPresent()).isEqualTo(true);

        for (PostsTag findByContent : findByContents) {
            log.info("findByContent.getContents() = {}", findByContent.getContents());
        }

        //결과 ok


    }


    @Test
    @DisplayName("PostsTagRepository findByAll 테스트")
    void posts_tag_repository_findByAll_test() throws Exception {
        // given

        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        PostsTag tag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        // when

        List<PostsTag> all = postsTagRepository.findAll();

        // then
        assertThat(Optional.ofNullable(all).isPresent()).isEqualTo(true);

        for (PostsTag postsTag : all) {
            log.info("postsTag.getContents() = {}", postsTag.getContents());
        }

        // 결과ok

    }


    @Test
    @DisplayName("PostsTagRepository의 findByPostId 메서드 jpql 테스트")
    void PostsTagRepository_findByPostId_jpql_test() throws Exception {
        // given

        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        PostsTag tag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        // when
        Post byPostId = postsTagRepository.findPostByPostId(post1.getId());

        // then
        assertThat(byPostId.getContent()).isEqualTo(post1.getContent());
        // 결과 ok

    }



}