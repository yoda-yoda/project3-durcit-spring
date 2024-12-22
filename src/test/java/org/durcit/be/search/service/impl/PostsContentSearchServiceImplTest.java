package org.durcit.be.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.search.dto.PostsContentSearchRequest;
import org.durcit.be.search.service.PostsContentSearchService;
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
class PostsContentSearchServiceImplTest {

    @Autowired
    private PostsContentSearchService postsContentSearchService;
    @Autowired
    private PostRepository postRepository;



    @Test
    @DisplayName("포스트3개, 해당 내용 2개 있지만 모두 delete true일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트1")
    void getAllPostCardResponsesWithNoneDeleted_method_test1() throws Exception {

        // given
        Post post1 = Post.builder()
                .content("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .content("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .content("바람의나라 정보글")
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


        PostsContentSearchRequest request = new PostsContentSearchRequest();
        request.setContent("메이플");


        // when
        List<PostCardResponse> list = postsContentSearchService.getAllPostCardResponsesWithNoneDeleted(request);

        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트


        // 결과 ok => 빈 리스트 반환.


    }



    @Test
    @DisplayName("포스트3개, 검색 내용이 아예 없을때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트2")
    void getAllPostCardResponsesWithNoneDeleted_method_test2() throws Exception {

        // given
        Post post1 = Post.builder()
                .content("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .content("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .content("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        PostsContentSearchRequest request = new PostsContentSearchRequest();
        request.setContent("어둠의전설");


        // when
        List<PostCardResponse> list = postsContentSearchService.getAllPostCardResponsesWithNoneDeleted(request);

        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트

        // 결과 ok => 빈 리스트 반환.


    }


    @Test
    @DisplayName("포스트3개, 해당 내용2개 있고 1개만 delete true일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트3")
    void getAllPostCardResponsesWithNoneDeleted_method_test3() throws Exception {

        // given
        Post post1 = Post.builder()
                .content("메이플스토리 정보글")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .content("메이플스토리 재미있다")
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .content("바람의나라 정보글")
                .build();
        postRepository.save(post3);


        Optional<Post> byId1 = postRepository.findById(1L);
        assertThat(byId1.isPresent()).isEqualTo(true);
        Post findPost1 = byId1.get();
        findPost1.setDeleted(true);
        postRepository.save(findPost1);


        PostsContentSearchRequest request = new PostsContentSearchRequest();
        request.setContent("메이플");


        // when
        List<PostCardResponse> list = postsContentSearchService.getAllPostCardResponsesWithNoneDeleted(request);

        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동.

        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto가 잘만들어졌는지까지는 알수없었다.
        // 다만 fromEntity에서 오류가 나는거라면 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다.




    }

}