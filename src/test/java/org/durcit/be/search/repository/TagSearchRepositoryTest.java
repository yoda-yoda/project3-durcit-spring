package org.durcit.be.search.repository;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.repository.PostsTagRepository;
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

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class TagSearchRepositoryTest {

    @Autowired
    TagSearchRepository tagSearchRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostsTagRepository postsTagRepository;





    @Test
    @DisplayName("Post 2개, PostsTag 1개, 검색한 태그가 PostsTag 테이블에 아예 없을때 - findPostsTagByContentsWithNoneDeleted 메서드 테스트2")
    void findPostsTagByContentsWithNoneDeleted_method_test2() throws Exception {

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


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(postTag1);


        // when

        List<PostsTag> list1 = tagSearchRepository.findPostsTagByContentsWithNoneDeleted("없는태그");

        // then

        log.info("list1.size() = {}", list1.size()); //0
        log.info("list1.toString() = {}", list1.toString()); // [] (빈리스트)

    }


    @Test
    @DisplayName("Post 2개, PostsTag 1개, 모두 delete false일때 - findPostsTagByContentsWithNoneDeleted 메서드 테스트1")
    void findPostsTagByContentsWithNoneDeleted_method_test1() throws Exception {

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


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(postTag1);


        // when

        List<PostsTag> list1 = tagSearchRepository.findPostsTagByContentsWithNoneDeleted("메이플스토리");

    	  // then

        log.info("list1.size() = {}", list1.size()); //1
        log.info("list1.get(0).getId() = {}", list1.get(0).getContents()); // 메이플스토리

    }




    @Test
    @DisplayName("Post 2개, PostsTag 1개, 검색한 태그가 PostsTag 테이블에 있긴한데 delete true 일때 - findPostsTagByContentsWithNoneDeleted 메서드 테스트3")
    void findPostsTagByContentsWithNoneDeleted_method_test3() throws Exception {

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


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(postTag1);

        Optional<PostsTag> byId = postsTagRepository.findById(1L);
        assertThat(byId.isPresent()).isEqualTo(true);

        PostsTag postsTag = byId.get();
        postsTag.setDeleted(true);
        postsTagRepository.save(postsTag);


        // when

        List<PostsTag> list1 = tagSearchRepository.findPostsTagByContentsWithNoneDeleted("메이플스토리");

        // then

        log.info("list1.size() = {}", list1.size()); //0
        log.info("list1.toString() = {}", list1.toString()); // [] (빈리스트)

    }




    @Test
    @DisplayName("Post를 postId로 찾는데, delete가 true 일때 - findPostsByIdWithNoneDeleted 메서드 테스트1")
    void findPostsByIdWithNoneDeleted_test1() throws Exception {

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


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(postTag1);

        Optional<Post> byId = postRepository.findById(1L);
        assertThat(byId.isPresent()).isEqualTo(true);

        Post post = byId.get();
        post.setDeleted(true);
        postRepository.save(post);

        // when

        List<Post> list = tagSearchRepository.findPostsByIdWithNoneDeleted(1L);


        // then

        log.info("list.size() = {}", list.size()); //0
        log.info("list.toString() = {}", list.toString()); // []


    }


    @Test
    @DisplayName("Post가 delete false일때 잘 가져오는지 - findPostsByIdWithNoneDeleted 메서드 테스트2")
    void findPostsByIdWithNoneDeleted_test2() throws Exception {

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


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(postTag1);


        // when

        List<Post> list = tagSearchRepository.findPostsByIdWithNoneDeleted(1L);


        // then

        log.info("list.size() = {}", list.size()); //1
        Post post = list.get(0);
        assertThat(post.getContent()).isEqualTo("내용1");
        log.info("post.getContent() = {}", post.getContent());


    }



    @Test
    @DisplayName("해당 PostId의 Post가 DB에 아예 존재하지않을때 - findPostsByIdWithNoneDeleted 메서드 테스트3")
    void findPostsByIdWithNoneDeleted_test3() throws Exception {

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


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(postTag1);


        // when

        List<Post> list = tagSearchRepository.findPostsByIdWithNoneDeleted(3L);


        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // []


    }









}