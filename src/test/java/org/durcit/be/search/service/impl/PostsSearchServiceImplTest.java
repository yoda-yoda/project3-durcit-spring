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
    @Autowired
    PostsSearchRepository postsSearchRepository;



    @Test
    @DisplayName("getAllPostsWithNoneDeleted 메서드 테스트")
    void getAllPostsWithNoneDeleted_method_test() throws Exception {
        // given
//        Post post1 = Post.builder()
//                .title("제목1")
//                .build();
//        postRepository.save(post1);
//
//        Optional<Post> byId1 = postRepository.findById(1L);
//        Post findPost1 = byId1.get();
//        findPost1.setDeleted(true);
//        postRepository.save(findPost1);
//
//        Post post2 = Post.builder()
//                .title("제목2")
//                .build();
//        postRepository.save(post2);
//
//        Post post3 = Post.builder()
//                .title("제목1")
//                .build();
//        postRepository.save(post3);




        // when

        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("제목1");

        List<Post> list = postsSearchService.getAllPostsWithNoneDeleted(request);



        // then

        // 직접 각 결과에 맞게 바꿔가면서 테스트해봄.
        // 결과1: 해당 제목의 Post가 아예 없다면 Not Found 오류o => 정상 작동
        // 결과2: 해당 제목의 Post가 2개 있고, delete 가 하나는 true이고 하나는 false 면 Not Found 오류x이고 해당 Post가 List에 담김.=> 정상 작동
        // 결과3: 해당 제목의 Post가 2개 있고, 둘다 delete 가 true이면 Not Found 오류o => 정상 작동



    }



    @Test
    @DisplayName("getAllPostCardResponsesWithNoneDeleted 메서드 테스트")
    void getAllPostCardResponsesWithNoneDeleted_method_test() throws Exception {

        // given
//        Post post1 = Post.builder()
//                .title("제목1")
//                .build();
//        postRepository.save(post1);
//
//        Optional<Post> byId1 = postRepository.findById(1L);
//        Post ffpost = byId1.get();
//        ffpost.setDeleted(true);
//        postRepository.save(ffpost);
//
//        Post post2 = Post.builder()
//                .title("제목2")
//                .build();
//        postRepository.save(post2);
//
//        Post post3 = Post.builder()
//                .title("제목1")
//                .build();
//        postRepository.save(post3);
//
//        Optional<Post> byId2 = postRepository.findById(3L);
//        Post fpost = byId2.get();
//        fpost.setDeleted(true);
//        postRepository.save(fpost);

        PostsSearchRequest request = new PostsSearchRequest();
        request.setTitle("제목3");

        postsSearchService.getAllPostCardResponsesWithNoneDeleted(request);



        // when

        // then

        // 직접 각 결과에 맞게 바꿔가면서 테스트해봄.
        // 그리고 포스트카드리스폰스의 fromEntity 메서드는, Post에 모든 값이 들어가있어야 작동하였다.
        // 그러나 이 테스트는 간략한 Post 엔티티만 만들어서 테스트 하는것이기때문에, 최종 Response dto가 잘만들어졌는지는 알수없었다.
        // 다만 fromEntity에서 오류가 나는거라면 그전의 로직은 통과한거기때문에 그것으로 추측함.

        // 결과1: 해당 제목의 Post가 아예 없다면  Not Found 오류o => 정상 작동
        // 결과2: 해당 제목의 Post가 있고, 그게 delete true 라면 Not Found 오류o => 정상 작동
        // 결과3: 해당 제목의 Post가 2개 있고, delete 가 하나는 true이고 하나는 false 면 Not Found 오류x. => 정상 작동
        // 결과4: 해당 제목의 Post가 2개 있고, 둘다 delete 가 true이면 Not Found 오류o => 정상 작동


    }





}