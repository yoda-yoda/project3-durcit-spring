package org.durcit.be.postsTag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostService;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.system.exception.tag.OptionalEmptyPostsTagFindByIdException;
import org.hibernate.dialect.TiDBDialect;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@Transactional
@Rollback(false)
@SpringBootTest
class PostsTagServiceTest {

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
    @DisplayName("createPostsTag 테스트1 => 3개의 임의의 게시글이 저장된 상태에서, createPostsTag 메서드 작동시 DB의 태그 테이블에 잘 저장되는지 테스트")
    void createPostsTag_method_test1() throws Exception {
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


        PostsTagRegisterRequest req1 = new PostsTagRegisterRequest();
        PostsTagRegisterRequest req2 = new PostsTagRegisterRequest();
        PostsTagRegisterRequest req3 = new PostsTagRegisterRequest();

        req1.setContents("메이플스토리");
        req2.setContents("바람의나라");
        req3.setContents("젤다의전설");

        List<PostsTagRegisterRequest> list = new ArrayList<>();
        list.add(req1);
        list.add(req2);
        list.add(req3);

        // when
        postsTagService.createPostsTag(list, 2L);


        // then
        // 테이블 잘 저장됐는지까지만 실제 디비에서 확인하기.
        // 결과: OK

    }

    @Test
    @DisplayName("createPostsTag 테스트2 => 3개의 임의의 게시글이 저장된 상태에서, createPostsTag 메서드 작동시 응답Dto 반환이 잘되는지 테스트")
    void createPostsTag_method_test2() throws Exception {
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


        PostsTagRegisterRequest req1 = new PostsTagRegisterRequest();
        PostsTagRegisterRequest req2 = new PostsTagRegisterRequest();
        PostsTagRegisterRequest req3 = new PostsTagRegisterRequest();

        req1.setContents("1메이플스토리");
        req2.setContents("2바람의나라");
        req3.setContents("3젤다의전설");

        List<PostsTagRegisterRequest> list = new ArrayList<>();
        list.add(req1);
        list.add(req2);
        list.add(req3);


        // when
        List<PostsTagResponse> postsTagResponseList = postsTagService.createPostsTag(list, 2L);

        // then
        int i = 0;

        for (PostsTagResponse res : postsTagResponseList) {

            assertThat(res.getContents()).isEqualTo(list.get(i).getContents());

            log.info("res.getContents() = {}", res.getContents());
            log.info("list.get(i).getContents() = {}", list.get(i).getContents());

            i++;
        }

        // 결과: ok
    }



    
    @Test
    @DisplayName("PostsTag soft delete 처리 후, getAllPostsTagsWithDeleted 메서드 (소프트딜리트된것 뺀 모든 엔티티를 리스트로 반환하는 메서드) 작동 테스트")
    void getAllPostsTagsWithDeleted_method_test() throws Exception {
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
                .contents("1바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("2메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("3메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        Optional<PostsTag> byId = postsTagRepository.findById(tag2.getId());
        assertThat(byId.isPresent()).isEqualTo(true);

        PostsTag postsTag2 = byId.get();

        // when
        postsTag2.setDeleted(true); // 태그 테이블에서 2번 id를 소프트딜리트 처리.
        postsTagRepository.save(postsTag2);

        // then
        List<PostsTag> allPostsTagsWithDeleted = postsTagService.getAllPostsTagsWithNonDeleted();

        assertThat(allPostsTagsWithDeleted.size()).isEqualTo(2);

        //결과ok, 디비에서도 딜리트 true 확인.
    }
    


    @Test
    @DisplayName("PostTag soft delete 처리 후에 getAllPostsTags 메서드 (딜리트든아니든 모든 엔티티를 리스트로 반환하는 메서드) 작동 테스트")
    void getAllPostsTags_method_test() throws Exception {

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
                .contents("1바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("2메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("3메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        Optional<PostsTag> byId = postsTagRepository.findById(tag2.getId());
        assertThat(byId.isPresent()).isEqualTo(true);

        PostsTag postsTag2 = byId.get();

        // when
        postsTag2.setDeleted(true); // 태그 테이블에서 2번 id를 소프트딜리트 처리.
        postsTagRepository.save(postsTag2);

        // then
        List<PostsTag> allPostsTags = postsTagService.getAllPostsTags();

        assertThat(allPostsTags.size()).isEqualTo(3);

        //결과ok, 디비에서 딜리트 true 지만 모두 가져온것 확인.
    }

    @Transactional
    @Rollback
    @Test
    @DisplayName("getPostsTagById 메서드 (태그테이블 중에 pk를 기준으로 찾고 소프트딜리트 true면 오류, false면 해당 엔티티 1개 반환하는 메서드) 작동 테스트")
    void getPostsTagById_method_test() throws Exception {
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
                .contents("1바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("2메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("3메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

        Optional<PostsTag> byId = postsTagRepository.findById(tag2.getId());
        assertThat(byId.isPresent()).isEqualTo(true);

        PostsTag postsTag2 = byId.get();


        // when
        postsTag2.setDeleted(true); // 태그 테이블에서 2번 id를 소프트딜리트 처리.
        postsTagRepository.save(postsTag2);

        // then
        assertThatThrownBy(() -> postsTagService.getPostsTagById(2L)).isInstanceOf(RuntimeException.class);

        // 결과 ok인데, rollback false 를 빼줘야 잘 통과한다.
        // 왜냐면 테스트 코드에서 오류나면 트랜젝션을 자동 rollback하도록 되어있기때문인듯하다.
        // 그래서 이 메서드위에 어노테이션을 달아놨다.


    }


    @Transactional
    @Test
    @DisplayName("getPostsTagListByPostId 메서드 (Post pk를 기준으로 Post 엔티티를 조회하고, 해당 엔티티 내부의 연관관계인 PostsTagList를 뽑아서 소프트 딜리트든 아니든 반환하는 메서드) 작동 테스트")
    void getPostsTagListByPostId_method_test() throws Exception {
        // given

//        Post post1 = Post.builder()
//                .content("내용1")
//                .views(1L)
//                .title("제목1")
//                .build();
//
//        Post post2 = Post.builder()
//                .content("내용2")
//                .views(1L)
//                .title("제목2")
//                .build();
//
//        Post post3 = Post.builder()
//                .content("내용3")
//                .views(1L)
//                .title("제목3")
//                .build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//
//        PostsTag tag1 = PostsTag.builder()
//                .contents("1바람의나라")
//                .post(post2)
//                .build();
//
//        PostsTag tag2 = PostsTag.builder()
//                .contents("2메이플스토리")
//                .post(post2)
//                .build();
//
//        PostsTag tag3 = PostsTag.builder()
//                .contents("3메이플스토리")
//                .post(post1)
//                .build();
//
//        postsTagRepository.save(tag1);
//        postsTagRepository.save(tag2);
//        postsTagRepository.save(tag3);



    	  // when
        List<PostsTag> list1 = postsTagService.getPostsTagListByPostId(1L);
        List<PostsTag> list2 = postsTagService.getPostsTagListByPostId(2L);
        List<PostsTag> list3 = postsTagService.getPostsTagListByPostId(3L);

        // then

    //     assertThat(list1.size()).isEqualTo(1);
        log.info("list1.size() = {}", list1.size());

    //     assertThat(list2.size()).isEqualTo(2);
        log.info("list2.size() = {}", list2.size());

    //     assertThat(list3.size()).isEqualTo(0);
        log.info("list3.size() = {}", list3.size());

        assertThat(list3).isNotNull();
        assertThat(list3).isEmpty();


        //  결과 ok 인데, 이거 통과 하려면 1번째 실행땐 저장까지만(when 전까지만) 먼저 한번 돌리고, 2번째 실행땐 저장까지는 주석처리하고 ddl 오토를 none으로 바꾸고 테스트해야 한다.
        //  트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기때문인듯하다.

    }


    @Test
    @DisplayName("getNoneDeletedPostsTagListByPostId 메서드 테스트")
    void getNoneDeletedPostsTagListByPostId_method_test() throws Exception {
        // given

//        Post post1 = Post.builder()
//                .content("내용1")
//                .views(1L)
//                .title("제목1")
//                .build();
//
//        Post post2 = Post.builder()
//                .content("내용2")
//                .views(1L)
//                .title("제목2")
//                .build();
//
//        Post post3 = Post.builder()
//                .content("내용3")
//                .views(1L)
//                .title("제목3")
//                .build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//
//        PostsTag tag1 = PostsTag.builder()
//                .contents("1바람의나라")
//                .post(post2)
//                .build();
//
//        PostsTag tag2 = PostsTag.builder()
//                .contents("2메이플스토리")
//                .post(post2)
//                .build();
//
//        PostsTag tag3 = PostsTag.builder()
//                .contents("3메이플스토리")
//                .post(post1)
//                .build();
//
//        postsTagRepository.save(tag1);
//        postsTagRepository.save(tag2);
//        postsTagRepository.save(tag3);

//        List<PostsTag> list1 = postsTagService.getNoneDeletedPostsTagListByPostId(2L);
//
//        for (PostsTag postsTag : list1) {
//            log.info("postsTag.getContents() = {}", postsTag.getContents());
//        }
//
//        postsTagService.deleteOnePostsTagByPostsTagId(2L);

    	  // when
//        List<PostsTag> list2 = postsTagService.getNoneDeletedPostsTagListByPostId(2L);
//
//        log.info("list2.get(0).getContents() = {}", list2.get(0).getContents());
//        log.info("list2.get(0).getId() = {}", list2.get(0).getId());
//
//        // then
//        assertThat(list2.size()).isEqualTo(1);
//        log.info("============================");
//        log.info("list2.size() = {}", list2.size());
//          // 결과: ok
        
        
        
        //지우기
      //  postsTagService.deleteOnePostsTagByPostsTagId(1L);





    }





    @Test
    @DisplayName("getPostsTagResponseListByPostId 메서드 (위 메서드처럼 PostsTagList 를 뽑되, 거기서 한번더 Response로 변환하여 반환해주는 메서드) 작동 테스트")
    void getPostsTagResponseListByPostId_method_test() throws Exception {
        // given

//        Post post1 = Post.builder()
//                .content("내용1")
//                .views(1L)
//                .title("제목1")
//                .build();
//
//        Post post2 = Post.builder()
//                .content("내용2")
//                .views(1L)
//                .title("제목2")
//                .build();
//
//        Post post3 = Post.builder()
//                .content("내용3")
//                .views(1L)
//                .title("제목3")
//                .build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//
//        PostsTag tag1 = PostsTag.builder()
//                .contents("1바람의나라")
//                .post(post2)
//                .build();
//
//        PostsTag tag2 = PostsTag.builder()
//                .contents("2메이플스토리")
//                .post(post2)
//                .build();
//
//        PostsTag tag3 = PostsTag.builder()
//                .contents("3메이플스토리")
//                .post(post1)
//                .build();
//
//        postsTagRepository.save(tag1);
//        postsTagRepository.save(tag2);
//        postsTagRepository.save(tag3);



        // when
        List<PostsTagResponse> responseList1 = postsTagService.getPostsTagResponseListByPostId(1L);
        List<PostsTagResponse> responseList2 = postsTagService.getPostsTagResponseListByPostId(2L);
        List<PostsTagResponse> responseList3 = postsTagService.getPostsTagResponseListByPostId(3L);

        // then

//             assertThat(responseList1.size()).isEqualTo(1);
//        log.info("list1.size() = {}", responseList1.size());
//
//             assertThat(responseList2.size()).isEqualTo(2);
//        log.info("list2.size() = {}", responseList2.size());
//
//             assertThat(responseList3.size()).isEqualTo(0);
//        log.info("list3.size() = {}", responseList3.size());
//
//        log.info("responseList2.get(0).getContents() = {}", responseList2.get(0).getContents());
//        log.info("responseList2.get(0).getContents() = {}", responseList2.get(1).getContents());
//
//
//        assertThat(responseList2.get(0).getContents()).isEqualTo(postsTagRepository.findById(1L).get().getContents());
//        assertThat(responseList2.get(1).getContents()).isEqualTo(postsTagRepository.findById(2L).get().getContents());
//
//
//        assertThat(responseList3).isNotNull();
//        assertThat(responseList3).isEmpty();
        
        //  결과 ok 인데, 이거 통과 하려면 1번째 실행땐 저장까지만(when 전까지만) 먼저 한번 돌리고, 2번째 실행땐 저장까지는 주석처리하고 ddl 오토를 none으로 바꾸고 테스트해야 한다.
        //  트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기때문인듯하다.
    }
    


    @Test
    @DisplayName("getPostsTagByContents 메서드 (유저가 검색한 1개의 contents를 담은 요청 Dto를 매개변수로 받고, 그걸 기준으로 일치하는 엔티티들 중에 소프트딜리트를 제외하고 List로 반환하는 메서드) 작동 테스트")
    void getPostsTagByContents_method_test() throws Exception {
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

        Post post4 = Post.builder()
                .content("내용4")
                .views(1L)
                .title("제목4")
                .build();

        Post post5 = Post.builder()
                .content("내용5")
                .views(1L)
                .title("제목5")
                .build();


        Post post6 = Post.builder()
                .content("내용6")
                .views(1L)
                .title("제목6")
                .build();


        Post post7 = Post.builder()
                .content("내용7")
                .views(1L)
                .title("제목7")
                .build();


        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);
        postRepository.save(post7);


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

        PostsTag tag4 = PostsTag.builder()
                .contents("바람의나라")
                .post(post1)
                .build();

        PostsTag tag5 = PostsTag.builder()
                .contents("롤")
                .post(post3)
                .build();

        PostsTag tag6 = PostsTag.builder()
                .contents("롤")
                .post(post4)
                .build();

        PostsTag tag7 = PostsTag.builder()
                .contents("롤")
                .post(post5)
                .build();

        PostsTag tag8 = PostsTag.builder()
                .contents("롤")
                .post(post6)
                .build();

        PostsTag tag9 = PostsTag.builder()
                .contents("바람의나라")
                .post(post6)
                .build();

        PostsTag tag10 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post7)
                .build();


        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);
        postsTagRepository.save(tag4);
        postsTagRepository.save(tag5);
        postsTagRepository.save(tag6);
        postsTagRepository.save(tag7);
        postsTagRepository.save(tag8);
        postsTagRepository.save(tag9);
        postsTagRepository.save(tag10);

        PostsTagRegisterRequest request = new PostsTagRegisterRequest();
        request.setContents("메이플스토리");


        // when
        List<PostsTag> list = postsTagService.getPostsTagByContents(request);

        // then
        for (PostsTag postsTag : list) {
            log.info("postsTag.getContents() = {}", postsTag.getContents());
        }

        assertThat(list.size()).isEqualTo(3);

        // 결과 ok

    }



    @Test
    @DisplayName("updatePostsTag 메서드 테스트")
    void updatePostsTag_method_test() throws Exception {
        // given
//        Post post1 = Post.builder()
//                .content("내용1")
//                .views(1L)
//                .title("제목1")
//                .build();
//
//        Post post2 = Post.builder()
//                .content("내용2")
//                .views(1L)
//                .title("제목2")
//                .build();
//
//        Post post3 = Post.builder()
//                .content("내용3")
//                .views(1L)
//                .title("제목3")
//                .build();
//
//        Post post4 = Post.builder()
//                .content("내용4")
//                .views(1L)
//                .title("제목4")
//                .build();
//
//        Post post5 = Post.builder()
//                .content("내용5")
//                .views(1L)
//                .title("제목5")
//                .build();
//
//
//        Post post6 = Post.builder()
//                .content("내용6")
//                .views(1L)
//                .title("제목6")
//                .build();
//
//
//        Post post7 = Post.builder()
//                .content("내용7")
//                .views(1L)
//                .title("제목7")
//                .build();
//
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//        postRepository.save(post4);
//        postRepository.save(post5);
//        postRepository.save(post6);
//        postRepository.save(post7);
//
//
//        PostsTag tag1 = PostsTag.builder()
//                .contents("바람의나라")
//                .post(post2)
//                .build();
//
//        PostsTag tag2 = PostsTag.builder()
//                .contents("메이플스토리")
//                .post(post2)
//                .build();
//
//        PostsTag tag3 = PostsTag.builder()
//                .contents("메이플스토리")
//                .post(post1)
//                .build();
//
//        PostsTag tag4 = PostsTag.builder()
//                .contents("바람의나라")
//                .post(post1)
//                .build();
//
//        PostsTag tag5 = PostsTag.builder()
//                .contents("롤")
//                .post(post3)
//                .build();
//
//        PostsTag tag6 = PostsTag.builder()
//                .contents("롤")
//                .post(post4)
//                .build();
//
//        PostsTag tag7 = PostsTag.builder()
//                .contents("롤")
//                .post(post5)
//                .build();
//
//        PostsTag tag8 = PostsTag.builder()
//                .contents("롤")
//                .post(post6)
//                .build();
//
//        PostsTag tag9 = PostsTag.builder()
//                .contents("바람의나라")
//                .post(post6)
//                .build();
//
//        PostsTag tag10 = PostsTag.builder()
//                .contents("메이플스토리")
//                .post(post7)
//                .build();
//
//
//        postsTagRepository.save(tag1);
//        postsTagRepository.save(tag2);
//        postsTagRepository.save(tag3);
//        postsTagRepository.save(tag4);
//        postsTagRepository.save(tag5);
//        postsTagRepository.save(tag6);
//        postsTagRepository.save(tag7);
//        postsTagRepository.save(tag8);
//        postsTagRepository.save(tag9);
//        postsTagRepository.save(tag10);


        // =================

//        List<PostsTagRegisterRequest> list = new ArrayList<>();
//        PostsTagRegisterRequest request1 = new PostsTagRegisterRequest();
//        PostsTagRegisterRequest request2 = new PostsTagRegisterRequest();
//        PostsTagRegisterRequest request3 = new PostsTagRegisterRequest();
//
//        request1.setContents("어둠의전설");
//        request2.setContents("바람의나라");
//        request3.setContents("메이플스토리");
//
//        list.add(request1);
//        list.add(request2);
//        list.add(request3);
//
//        // when
//
//    	  // then
//        postsTagService.updatePostsTag(list, 6L);


        // 결과는 ok. 디비에서 직접 확인해봤다.
        // 그런데 이 메서드도 통과 하려면 또 별도로 작업해서 2번 실행해줘야함.
        //  트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기 때문인듯하다.

        // 이대로 했을시 통과 조건 =>
        // 포스트 6번 id의 게시글안의 태그들은 (=>롤,바람의나라) 전부 소프트딜리트가 true 돼야함.
        // 또한 포스트 6번 글에 3개의 태그가 새로 추가돼야함.



    }


    @Test
    @DisplayName("deleteOnePostsTagByPostsTagId 메서드 테스트")
    void deleteOnePostsTagByPostsTagId_method_test() throws Exception {
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
                .contents("1바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("2메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("3메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

    	  // when

    	  // then
        postsTagService.deleteOnePostsTagByPostsTagId(2L);

        assertThat(postsTagRepository.findById(2L).orElseThrow().isDeleted()).isEqualTo(true);
        log.info("postsTagRepository.findById(2L).orElseThrow().isDeleted() = {}", postsTagRepository.findById(2L).orElseThrow().isDeleted());

        // 결과ok.
    }



    @Test
    @DisplayName("deletePostsTagsByPostsTagId 메서드 테스트")
    void deletePostsTagsByPostsTagId_method_test() throws Exception {
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
                .contents("1바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("2메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("3메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

    	  // when
        List<Long> list = new ArrayList<>();
        list.add(tag1.getId());
        list.add(tag2.getId());


    	  // then

        postsTagService.deletePostsTagsByPostsTagId(list);

        assertThat(postsTagRepository.findById(tag1.getId()).orElseThrow().isDeleted()).isEqualTo(true);
        assertThat(postsTagRepository.findById(tag2.getId()).orElseThrow().isDeleted()).isEqualTo(true);
        assertThat(postsTagRepository.findById(tag3.getId()).orElseThrow().isDeleted()).isEqualTo(false);

        log.info("postsTagRepository.findById(tag1.getId()).orElseThrow().isDeleted() = {}", postsTagRepository.findById(tag1.getId()).orElseThrow().isDeleted());
        log.info("postsTagRepository.findById(tag2.getId()).orElseThrow().isDeleted() = {}", postsTagRepository.findById(tag2.getId()).orElseThrow().isDeleted());
        log.info("postsTagRepository.findById(tag3.getId()).orElseThrow().isDeleted() = {}", postsTagRepository.findById(tag3.getId()).orElseThrow().isDeleted());

        // 결과ok.

    }


    @Test
    @DisplayName("deletePostsTagsByPostId 메서드 테스트")
    void deletePostsTagsByPostId_method_test() throws Exception {
         // given
//        Post post1 = Post.builder()
//                .content("내용1")
//                .views(1L)
//                .title("제목1")
//                .build();
//
//        Post post2 = Post.builder()
//                .content("내용2")
//                .views(1L)
//                .title("제목2")
//                .build();
//
//
//        Post post3 = Post.builder()
//                .content("내용3")
//                .views(1L)
//                .title("제목3")
//                .build();
//
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//
//        PostsTag tag1 = PostsTag.builder()
//                .contents("1바람의나라")
//                .post(post2)
//                .build();
//
//        PostsTag tag2 = PostsTag.builder()
//                .contents("2메이플스토리")
//                .post(post2)
//                .build();
//
//        PostsTag tag3 = PostsTag.builder()
//                .contents("3메이플스토리")
//                .post(post1)
//                .build();
//
//        postsTagRepository.save(tag1);
//        postsTagRepository.save(tag2);
//        postsTagRepository.save(tag3);


//    	  // when
//
//        List<PostsTag> list = postsTagService.getPostsTagListByPostId(2L);
//        assertThat(list.size()).isEqualTo(2);
//        log.info("list.size() = {}", list.size());
//
//        for (PostsTag postsTag : list) {
//            assertThat(postsTag.isDeleted()).isEqualTo(false);
//            log.info("postsTag.isDeleted() = {}", postsTag.isDeleted());
//        }
//
//
//    	  // then
//        postsTagService.deletePostsTagsByPostId(2L);
//
//        List<PostsTag> list2 = postsTagService.getPostsTagListByPostId(2L);
//        log.info("list.size() = {}", list.size()); //2
//        for (PostsTag postsTag : list2) {
//            assertThat(postsTag.isDeleted()).isEqualTo(true);
//            log.info("postsTag.isDeleted() = {}", postsTag.isDeleted());
//        }
              // 결과 ok.



    }






    @Test
    @DisplayName("하나의 테스트 메서드안에서 Post안의 TagList를 확인하면 자꾸 null이 뜨는것때문에 귀찮아서, 그냥 ddl 오토로 테이블만 만든 테스트 메서드")
    void confirm_tagList_test_method() throws Exception {

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
                .contents("1바람의나라")
                .post(post2)
                .build();

        PostsTag tag2 = PostsTag.builder()
                .contents("2메이플스토리")
                .post(post2)
                .build();

        PostsTag tag3 = PostsTag.builder()
                .contents("3메이플스토리")
                .post(post1)
                .build();

        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);

    }





     @Test
     @DisplayName("Post 엔티티안에 연관관계 변수인 List<PostsTag> postsTagList 는 참조가 어떤식으로 되는지 확인하는 메서드")
     void post_entity_postsTagList_check() throws Exception {
//        // given
//        // 임시 Post 엔티티를 총 3개 생성하고 DB에 저장한다. 그리고 태그가 "메이플스토리", "바람의나라" 인 PostsTag 엔티티를 총 2개 생성하고 DB에 저장한다.
//        // 이때 post 필드에 2번 게시물 엔티티를 넣고 저장할것이다.
//
//        Post post1 = Post.builder()
//                .views(1L)
//                .title("title1")
//                .content("content1")
//                .build();
//
//        Post post2 = Post.builder()
//                .views(1L)
//                .title("title2")
//                .content("content2")
//                .build();
//
//        Post post3 = Post.builder()
//                .views(1L)
//                .title("title3")
//                .content("content3")
//                .build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//
//        PostsTag postsTag1 = PostsTag.builder()
//                .contents("메이플스토리")
//                .post(post2)
//                .build();
//
//        PostsTag postsTag2 = PostsTag.builder()
//                .contents("바람의나라")
//                .post(post2)
//                .build();
//
//        postsTagRepository.save(postsTag1);
//        postsTagRepository.save(postsTag2);
//
//
//        Post findPost = postRepository.findById(2L).orElseThrow();
//        log.info("findPost.getContent() = {}", findPost.getContent());
//
//
//        // when
//        List<PostsTag> postsTagList = findPost.getPostsTagList();
//
//
//        // then
//        assertThat(postsTagList.size()).isEqualTo(2);
//
//        // 결론 =>  이대로면 항상 fail일것이다. 하나의 트랜젝션이 끝나고나서야 그때서야 List가 자동주입되는듯하다.
//
    }
    





}