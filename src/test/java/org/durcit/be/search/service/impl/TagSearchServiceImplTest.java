package org.durcit.be.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
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

@SpringBootTest
@Slf4j
@Transactional
@Rollback(false)
class TagSearchServiceImplTest {


    @Autowired
    TagSearchService tagSearchService;
    @Autowired
    TagSearchRepository tagSearchRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostsTagRepository postsTagRepository;



    @Test
    @DisplayName("Post 3개, PostsTag 3개, 1개의 Tag만 delete true 일때 - getAllPostCardResponsesWithNoneDeletedPostsTag 메서드 테스트1")
    void getAllPostCardResponsesWithNoneDeletedPostsTag_method_test1() throws Exception {


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

        Post post3 = Post.builder()
                .content("내용3")
                .views(1L)
                .title("제목3")
                .build();


        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        PostsTag postTag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag postTag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post3)
                .build();

        postsTagRepository.save(postTag1);
        postsTagRepository.save(postTag2);
        postsTagRepository.save(postTag3);


        Optional<PostsTag> byId = postsTagRepository.findById(3L);
        assertThat(byId.isPresent()).isEqualTo(true);

        PostsTag postsTag = byId.get();
        postsTag.setDeleted(true);
        postsTagRepository.save(postsTag);


        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<PostCardResponse> list = tagSearchService.getAllPostCardResponsesWithNoneDeletedPostsTag(request);

        // then

        // 결과 ok로 추측:
        // 임시로 만든 post 엔티티라 fromEntity 부분에서(응답Dto로 변환하는 부분에서) 걸리기때문에 완전한 테스트를 진행할수는 없었다.
        // 그러나 오류가 난 부분은 핵심 로직을 통과한 이후이기때문에 정상 작동이라고 추측가능하다.



    }





    @Test
    @DisplayName("Post 2개, PostsTag 1개, 태그가 delete true 일때 - getAllPostCardResponsesWithNoneDeletedPostsTag 메서드 테스트2")
    void getAllPostCardResponsesWithNoneDeletedPostsTag_method_test2() throws Exception {

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


        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<PostCardResponse> list = tagSearchService.getAllPostCardResponsesWithNoneDeletedPostsTag(request);

        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트

        // 결과 ok: 빈리스트 반환.

    }


    @Test
    @DisplayName("Post 2개, PostsTag 1개, 태그는 delete false 인데, Post가 true 일떄 - getAllPostCardResponsesWithNoneDeletedPostsTag 메서드 테스트3")
    void getAllPostCardResponsesWithNoneDeletedPostsTag_method_test3() throws Exception {

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



        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<PostCardResponse> list = tagSearchService.getAllPostCardResponsesWithNoneDeletedPostsTag(request);

        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트

        // 결과 ok: 빈리스트 반환.

    }


    @Test
    @DisplayName("Post 2개, PostsTag 1개 검색 태그가 존재하지않을때 - getAllPostCardResponsesWithNoneDeletedPostsTag 메서드 테스트4")
    void getAllPostCardResponsesWithNoneDeletedPostsTag_method_test4() throws Exception {

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



        TagSearchRequest request = new TagSearchRequest();
        request.setContents("바람의나라");

        // when

        List<PostCardResponse> list = tagSearchService.getAllPostCardResponsesWithNoneDeletedPostsTag(request);

        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트

        // 결과 ok: 빈리스트 반환.

    }




    @Test
    @DisplayName("Post 2개, PostsTag 1개, 검색 태그가 존재하고 모두 delete가 false 일때 - getAllPostsWithNoneDeletedPostsTag 메서드 테스트1")
    void getAllPostsWithNoneDeletedPostsTag_method_test1() throws Exception {

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

        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<Post> list = tagSearchService.getAllPostsWithNoneDeletedPostsTag(request);

        // then

        log.info("list.size() = {}", list.size()); //1
        Post post = list.get(0);
        assertThat(post.getId()).isEqualTo(post1.getId()); // 아이디 1L
        assertThat(post.getTitle()).isEqualTo("제목1");
        log.info("post.getTitle() = {}", post.getTitle()); // 제목1


    }



    @Test
    @DisplayName("Post 2개, PostsTag 1개, 검색한 태그의 delete가 true 일때 - getAllPostsWithNoneDeletedPostsTag 메서드 테스트2")
    void getAllPostsWithNoneDeletedPostsTag_method_test2() throws Exception {

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


        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<Post> list = tagSearchService.getAllPostsWithNoneDeletedPostsTag(request);

        // then

        // 결과 ok: 정의한 오류 발생 - 정상작동.



    }




    @Test
    @DisplayName("Post 2개, PostsTag 1개, 검색한 태그가 아예 존재하지 않을때 - getAllPostsWithNoneDeletedPostsTag 메서드 테스트3")
    void getAllPostsWithNoneDeletedPostsTag_method_test3() throws Exception {

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


        TagSearchRequest request = new TagSearchRequest();
        request.setContents("바람의나라");

        // when

        List<Post> list = tagSearchService.getAllPostsWithNoneDeletedPostsTag(request);


        // then

        // 결과 ok: 정의한 오류 발생 - 정상작동.



    }


    @Test
    @DisplayName("Post 2개, PostsTag 1개, 태그 delete는 false인데 해당 Post delete가 true 일때 - getAllPostsWithNoneDeletedPostsTag 메서드 테스트4")
    void getAllPostsWithNoneDeletedPostsTag_method_test4() throws Exception {

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


        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<Post> list = tagSearchService.getAllPostsWithNoneDeletedPostsTag(request);

        // then

        // 결과 ok: 정의한 오류 발생 - 정상작동.



    }



    @Test
    @DisplayName("Post 3개, PostsTag 3개, 1개의 Tag만 delete true 일때 - getAllPostsWithNoneDeletedPostsTag 메서드 테스트5")
    void getAllPostsWithNoneDeletedPostsTag_method_test5() throws Exception {

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

        Post post3 = Post.builder()
                .content("내용3")
                .views(1L)
                .title("제목3")
                .build();


        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();

        PostsTag postTag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();

        PostsTag postTag3 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post3)
                .build();

        postsTagRepository.save(postTag1);
        postsTagRepository.save(postTag2);
        postsTagRepository.save(postTag3);


        Optional<PostsTag> byId = postsTagRepository.findById(3L);
        assertThat(byId.isPresent()).isEqualTo(true);

        PostsTag postsTag = byId.get();
        postsTag.setDeleted(true);
        postsTagRepository.save(postsTag);


        TagSearchRequest request = new TagSearchRequest();
        request.setContents("메이플스토리");

        // when

        List<Post> list = tagSearchService.getAllPostsWithNoneDeletedPostsTag(request);

        // then

        log.info("list.size()================ = {}", list.size()); //2
        Post post = list.get(0);
        assertThat(post.getId()).isEqualTo(post1.getId()); // 아이디 1L
        assertThat(post.getTitle()).isEqualTo("제목1");
        log.info("post.getTitle() ============= {}", post.getTitle()); // 제목1

        Post postTwo = list.get(1);
        assertThat(postTwo.getId()).isEqualTo(post2.getId()); // 아이디 2L
        assertThat(postTwo.getTitle()).isEqualTo("제목2");
        log.info("post.getTitle() =============== {}", postTwo.getTitle()); // 제목2


    }



    




}