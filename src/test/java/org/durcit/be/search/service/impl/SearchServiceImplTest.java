package org.durcit.be.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.search.dto.SearchRequest;
import org.durcit.be.search.service.SearchService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.repository.MemberRepository;
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
class SearchServiceImplTest {


    @Autowired
    SearchService searchService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostsTagRepository postsTagRepository;
    @Autowired
    MemberRepository memberRepository;



    @Test
    @DisplayName("Post 2개, PostsTag 1개, 태그가 delete true 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트1")
    void getAllPostCardResponsesWithNoneDeleted_method_test1() throws Exception {


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


        SearchRequest request = new SearchRequest();
        request.setSearch("메이플스토리");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트


        // 결과 ok => 빈 리스트 반환.



    }




    @Test
    @DisplayName("Post 2개, PostsTag 1개, 모두 delete false 이고 검색에 해당하는게 아예 없을때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트2")
    void getAllPostCardResponsesWithNoneDeleted_method_test2() throws Exception {


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


        SearchRequest request = new SearchRequest();
        request.setSearch("어둠의전설");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트


        // 결과 ok => 빈 리스트 반환.

    }




    @Test
    @DisplayName("Post 2개, PostsTag 1개, 태그가 존재하고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트3")
    void getAllPostCardResponsesWithNoneDeleted_method_test3() throws Exception {


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



        SearchRequest request = new SearchRequest();
        request.setSearch("메이플");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.


    }


    @Test
    @DisplayName("Post 2개, PostsTag 1개, Post 제목이 1개 존재하고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트4")
    void getAllPostCardResponsesWithNoneDeleted_method_test4() throws Exception {


        // given
        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("메이플스토리 정보글")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();


        postRepository.save(post1);
        postRepository.save(post2);


        PostsTag postTag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("메이플");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.


    }


    @Test
    @DisplayName("Post 2개, PostsTag 1개, Post 내용이 1개 존재하고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트5")
    void getAllPostCardResponsesWithNoneDeleted_method_test5() throws Exception {


        // given
        Post post1 = Post.builder()
                .content("메이플스토리 정보글")
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
                .contents("바람의나라")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("메이플");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.


    }



    @Test
    @DisplayName("Member 2개, Post 2개, PostsTag 1개, 검색에 해당하는것 없고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트6")
    void getAllPostCardResponsesWithNoneDeleted_method_test6() throws Exception {


        // given

        Member mem1 = Member.builder()
                .nickname("첫째멤버")
                .email("aaa@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem1);


        Member mem2 = Member.builder()
                .nickname("둘째멤버")
                .email("bbb@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem2);


        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .member(mem1)
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .member(mem2)
                .build();


        postRepository.save(post1);
        postRepository.save(post2);


        PostsTag postTag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("셋째멤버");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        log.info("list.size() = {}", list.size()); // 0
        log.info("list.toString() = {}", list.toString()); // [] 빈리스트


        // 결과 ok => 빈 리스트 반환.


    }


    @Test
    @DisplayName("Member 2개, Post 2개, PostsTag 1개, 검색한 nickname 있고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트7")
    void getAllPostCardResponsesWithNoneDeleted_method_test7() throws Exception {


        // given

        Member mem1 = Member.builder()
                .nickname("첫째멤버")
                .email("aaa@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem1);


        Member mem2 = Member.builder()
                .nickname("둘째멤버")
                .email("bbb@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem2);


        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .member(mem1)
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .member(mem2)
                .build();


        postRepository.save(post1);
        postRepository.save(post2);


        PostsTag postTag1 = PostsTag.builder()
                .contents("바람의나라")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("첫");


        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.




    }


    @Test
    @DisplayName("Member 2개, Post 2개, PostsTag 1개, 검색한 tag 있고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트8")
    void getAllPostCardResponsesWithNoneDeleted_method_test8() throws Exception {


        // given

        Member mem1 = Member.builder()
                .nickname("첫째멤버")
                .email("aaa@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem1);


        Member mem2 = Member.builder()
                .nickname("둘째멤버")
                .email("bbb@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem2);


        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .member(mem1)
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .member(mem2)
                .build();


        postRepository.save(post1);
        postRepository.save(post2);


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("메이");



        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.




    }



    @Test
    @DisplayName("Member 2개, Post 2개, PostsTag 1개, 검색한 title 있고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트9")
    void getAllPostCardResponsesWithNoneDeleted_method_test9() throws Exception {


        // given

        Member mem1 = Member.builder()
                .nickname("첫째멤버")
                .email("aaa@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem1);


        Member mem2 = Member.builder()
                .nickname("둘째멤버")
                .email("bbb@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem2);


        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("메이플스토리 정보글")
                .member(mem1)
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .member(mem2)
                .build();


        postRepository.save(post1);
        postRepository.save(post2);


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("정보");



        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.




    }


    @Test
    @DisplayName("Member 2개, Post 2개, PostsTag 1개, 검색한 content 있고 모두 delete false 일때 - getAllPostCardResponsesWithNoneDeleted 메서드 테스트10")
    void getAllPostCardResponsesWithNoneDeleted_method_test10() throws Exception {


        // given

        Member mem1 = Member.builder()
                .nickname("첫째멤버")
                .email("aaa@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem1);


        Member mem2 = Member.builder()
                .nickname("둘째멤버")
                .email("bbb@naver.com")
                .isVerified(true)
                .build();

        memberRepository.save(mem2);


        Post post1 = Post.builder()
                .content("메이플스토리 정보글")
                .views(1L)
                .title("제목1")
                .member(mem1)
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .member(mem2)
                .build();


        postRepository.save(post1);
        postRepository.save(post2);


        PostsTag postTag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();


        postsTagRepository.save(postTag1);



        SearchRequest request = new SearchRequest();
        request.setSearch("정보");



        // when

        List<PostCardResponse> list = searchService.getAllPostCardResponsesWithNoneDeleted(request);


        // then

        // 결과 ok => fromEntity 부터 오류. 즉 그전까지 로직 정상작동이라고 추측가능.
        // 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하지만,
        // 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto까지 잘만들어졌는지는 알기어려웠다.
        // 다만 fromEntity 부분에서부터 오류가 나는거라면, 그전의 로직은 통과한거기때문에 그것으로 정상작동을 추측 가능하다. 원래는 post1 이 나와야함.




    }















}