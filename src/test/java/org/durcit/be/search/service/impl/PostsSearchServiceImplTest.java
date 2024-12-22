package org.durcit.be.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.search.dto.PostsSearchRequest;
import org.durcit.be.search.repository.PostsSearchRepository;
import org.durcit.be.search.service.PostsSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class PostsSearchServiceImplTest {


    @Autowired
    PostsSearchService postsSearchService;
    @Autowired
    PostRepository postRepository;

    
    
    @Test
    @DisplayName("Post 3개, 해당 제목 포함된게 2개이고 1개가 delete true일때  getAllPostsWithNoneDeleted 메서드 테스트1")
    void getAllPostsWithNoneDeleted_method_test1() throws Exception {
        // given
        Post post1 = Post.builder()
                .title("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        Optional<Post> byId1 = postRepository.findById(1L);
        assertThat(byId1.isPresent()).isEqualTo(true);
        Post findPost1 = byId1.get();
        findPost1.setDeleted(true);
        postRepository.save(findPost1);

        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("메이플");


        // when
        List<Post> list = postsSearchService.getAllPostsWithNoneDeleted(request);


        // then

        assertThat(list.size()).isEqualTo(1);
        Post post = list.get(0);
        assertThat(post.getId()).isEqualTo(2);
        log.info("post.getId() = {}", post.getId()); // 2
        log.info("post.getTitle() = {}", post.getTitle()); // 메이플스토리 재미있다


    }



    @Test
    @DisplayName("Post 3개, 해당 제목 아예없을때  getAllPostsWithNoneDeleted 메서드 테스트2")
    void getAllPostsWithNoneDeleted_method_test2() throws Exception {
        // given
        Post post1 = Post.builder()
                .title("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        Optional<Post> byId1 = postRepository.findById(1L);
        assertThat(byId1.isPresent()).isEqualTo(true);
        Post findPost1 = byId1.get();
        findPost1.setDeleted(true);
        postRepository.save(findPost1);

        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("어둠의전설");


        // when
        List<Post> list = postsSearchService.getAllPostsWithNoneDeleted(request);


        // then
        
        // 결과ok: 정의한 오류 발생 => 정상 작동.


    }



    @Test
    @DisplayName("Post 3개, 해당 제목2개있는데 전부 delete true 일때  getAllPostsWithNoneDeleted 메서드 테스트3")
    void getAllPostsWithNoneDeleted_method_test3() throws Exception {
        // given
        Post post1 = Post.builder()
                .title("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        Optional<Post> byId1 = postRepository.findById(1L);
        assertThat(byId1.isPresent()).isEqualTo(true);
        Post findPost1 = byId1.get();
        findPost1.setDeleted(true);
        postRepository.save(findPost1);

        Optional<Post> byId2 = postRepository.findById(2L);
        assertThat(byId2.isPresent()).isEqualTo(true);
        Post findPost2 = byId2.get();
        findPost2.setDeleted(true);
        postRepository.save(findPost2);



        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("메이플");


        // when
        List<Post> list = postsSearchService.getAllPostsWithNoneDeleted(request);


        // then
        // 결과ok: 정의한 오류 발생 => 정상 작동.


    }




    @Test
    @DisplayName("포스트3개, 해당 제목2개 있지만 모두 delete true일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트1")
    void getAllPostCardResponsesWithNoneDeleted_method_test1() throws Exception {

        // given
        Post post1 = Post.builder()
                .title("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        Optional<Post> byId1 = postRepository.findById(1L);
        assertThat(byId1.isPresent()).isEqualTo(true);
        Post findPost1 = byId1.get();
        findPost1.setDeleted(true);
        postRepository.save(findPost1);

        Optional<Post> byId2 = postRepository.findById(2L);
        assertThat(byId2.isPresent()).isEqualTo(true);
        Post findPost2 = byId2.get();
        findPost2.setDeleted(true);
        postRepository.save(findPost2);


        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("메이플");


        // when
        List<PostCardResponse> list = postsSearchService.getAllPostCardResponsesWithNoneDeleted(request);
        
        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트

      
        // 결과 ok => 빈 리스트 반환.





    }



    @Test
    @DisplayName("포스트3개, 검색 제목이 아예 없을때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트2")
    void getAllPostCardResponsesWithNoneDeleted_method_test2() throws Exception {

        // given
        Post post1 = Post.builder()
                .title("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("어둠의전설");


        // when
        List<PostCardResponse> list = postsSearchService.getAllPostCardResponsesWithNoneDeleted(request);

        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트

        // 결과 ok => 빈 리스트 반환.

    }


    @Test
    @DisplayName("포스트3개, 해당 제목2개 있고 1개만 delete true일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트3")
    void getAllPostCardResponsesWithNoneDeleted_method_test3() throws Exception {

        // given
        Post post1 = Post.builder()
                .title("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        Optional<Post> byId1 = postRepository.findById(1L);
        assertThat(byId1.isPresent()).isEqualTo(true);
        Post findPost1 = byId1.get();
        findPost1.setDeleted(true);
        postRepository.save(findPost1);


        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("메이플");


        // when
        List<PostCardResponse> list = postsSearchService.getAllPostCardResponsesWithNoneDeleted(request);

        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동.

        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto가 잘만들어졌는지까지는 알수없었다.
        // 다만 fromEntity에서 오류가 나는거라면 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다.




    }



}