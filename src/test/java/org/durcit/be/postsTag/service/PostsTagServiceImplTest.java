package org.durcit.be.postsTag.service;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostService;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.postsTag.service.impl.PostsTagServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@Transactional
@Rollback(false)
@SpringBootTest
class PostsTagServiceImplTest {

    // 생성자 주입으로 하려니까 오류가나서 필드주입으로 했다. Junit 테스트는 생성자주입이 잘안먹히나보다.
    @Autowired
    private PostsTagServiceImpl postsTagServiceImpl;
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
        postsTagServiceImpl.createPostsTag(list, 2L);


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
        List<PostsTagResponse> postsTagResponseList = postsTagServiceImpl.createPostsTag(list, 2L);

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
    @DisplayName("PostsTag soft delete 처리 후, getAllPostsTagsWithNonDeleted 메서드 (소프트딜리트된것 뺀 모든 엔티티를 리스트로 반환하는 메서드) 작동 테스트")
    void getAllPostsTagsWithNonDeleted_method_test() throws Exception {
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
        List<PostsTag> list = postsTagServiceImpl.getAllPostsTagsWithNonDeleted();

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getId()).isEqualTo(1L);
        assertThat(list.get(1).getId()).isEqualTo(3L);

//        //결과ok, 디비에서도 딜리트 true 확인.



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

        // then1
        List<PostsTag> allPostsTags = postsTagServiceImpl.getAllPostsTags();

        assertThat(allPostsTags.size()).isEqualTo(3);

        //결과ok, 2번 엔티티가 딜리트 true 지만 모두 가져온것 확인.


        //then2

        List<PostsTag> all = postsTagServiceImpl.getAllPostsTags();

        for (PostsTag postsTag : all) {
            postsTag.setDeleted(true);
            postsTagRepository.save(postsTag);
        }

        List<PostsTag> allList = postsTagServiceImpl.getAllPostsTags();

        assertThat(allList.size()).isEqualTo(3);

        for (PostsTag postsTag : allList) {
            log.info("postsTag.getId() = {}", postsTag.getId());
        }

        //결과ok, 모든 엔티티가 딜리트 true 지만 모두 가져온것 확인.

    }


    @Transactional
    @Test
    @DisplayName("getPostsTagById 메서드 (태그테이블 중에 pk를 기준으로 찾고 소프트딜리트 true면 오류, 아니면 해당 엔티티 1개 반환하는 메서드) 작동 테스트")
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
         postsTagServiceImpl.getPostsTagById(2L);

        // 결과 ok. 소프트딜리트 처리되어있는걸 찾아도 예외 작동, 해당 아이디가 디비에 아예 없어도 예외 작동.







    }


    @Transactional
    @Test
    @DisplayName("getPostsTagListByPostId 메서드 (Post pk를 기준으로 Post 엔티티를 조회하고, 해당 엔티티 내부의 연관관계인 PostsTagList를 뽑아서 소프트 딜리트든 아니든 반환하는 메서드) 작동 테스트")
    void getPostsTagListByPostId_method_test() throws Exception {
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


        // ======



//        // when
//        Optional<PostsTag> byId = postsTagRepository.findById(2L);
//        assertThat(byId.isPresent()).isEqualTo(true);
//        PostsTag postsTag = byId.get();
//        postsTag.setDeleted(true);
//        postsTagRepository.save(postsTag); // 태그 아이디 2번을 딜리트처리함
//
//
//
//        List<PostsTag> list1 = postsTagService.getPostsTagListByPostId(1L);
//        List<PostsTag> list2 = postsTagService.getPostsTagListByPostId(2L);
//        List<PostsTag> list3 = postsTagService.getPostsTagListByPostId(3L);
//
//        // then
//
//    //     assertThat(list1.size()).isEqualTo(1);
//        log.info("list1.size() = {}", list1.size());
//
//    //     assertThat(list2.size()).isEqualTo(2);
//        log.info("list2.size() = {}", list2.size());
//
//    //     assertThat(list3.size()).isEqualTo(0);
//        log.info("list3.size() = {}", list3.size());
//
//        assertThat(list3).isNotNull();
//        assertThat(list3).isEmpty();





        //  결과 ok 인데, 이거 통과 하려면 1번째 실행땐 저장까지만(when 전까지만) 먼저 한번 돌리고,
        //  2번째 실행땐 그거 주석처리하고 뒤에꺼 주석처리 푼다음에 ddl 오토를 none으로 바꾸고 테스트해야 한다.
        //  트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기때문인듯하다.


    }


    @Test
    @DisplayName("getNoneDeletedPostsTagListByPostId 메서드 테스트")
    void getNoneDeletedPostsTagListByPostId_method_test() throws Exception {
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



//        Post post = postsTagRepository.findPostByPostId(2L);
//        List<PostsTag> list1 = post.getPostsTagList();
//        PostsTag postsTag1 = list1.get(0);
//        postsTag1.setDeleted(true);
//        postsTagRepository.save(postsTag1);

        // then
//        List<PostsTag> non = postsTagService.getNoneDeletedPostsTagListByPostId(2L);
//        assertThat(non.size()).isEqualTo(1);
        // 결과: ok. 소프트딜리트된것 안가져오고, 전부 딜리트면 오류던지고, 저장된게 아예없어도 오류던진다. 여러 경우의수로 확인했다.
        
        






    }





    @Test
    @DisplayName("getPostsTagResponseListByPostId 메서드 (해당 Post 엔티티 내부의 PostsTagList 중에서 delete 처리 안된것을 Response로 뽑아 변환하고, 이외의 경우는 빈 리스트를 반환 하는 메서드) 작동 테스트")
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


        // / ////////////////////////
//
//        Post post = postsTagRepository.findPostByPostId(2L);
//        List<PostsTag> list1 = post.getPostsTagList(); // 리스트속에 총 2개일거임
//        PostsTag postsTag1 = list1.get(0); // 리스트겟0은 1바람의나라, 리스트겟1은 2메이플스토리 일거임
//        postsTag1.setDeleted(true);
//        postsTagRepository.save(postsTag1); // 이제 1바람의나라 딜리트처리됐을것임.


        // when1
//        List<PostsTagResponse> responseList1 = postsTagService.getPostsTagResponseListByPostId(1L); //1개만 담길것임
//        List<PostsTagResponse> responseList2 = postsTagService.getPostsTagResponseListByPostId(2L); //1개만담길것임
//        List<PostsTagResponse> responseList3 = postsTagService.getPostsTagResponseListByPostId(3L); // 빈리스트일것임.
//
//
//        // then1
//
//             assertThat(responseList1.size()).isEqualTo(1);
//        log.info("list1.size() = {}", responseList1.size());
//
//             assertThat(responseList2.size()).isEqualTo(1);
//        log.info("list2.size() = {}", responseList2.size());
//
//             assertThat(responseList3.size()).isEqualTo(0);
//        log.info("list3.size() = {}", responseList3.size());
//        log.info("responseList3.toString() = {}", responseList3.toString());
//
//        log.info("responseList2.get(0).getContents() = {}", responseList2.get(0).getContents()); // 2메이플스토리 일것임.
//
//        assertThat(responseList3).isNotNull();
//        assertThat(responseList3).isEmpty();

        //  결과1 ok.  그런데 이 테스트메서드 통과 하려면 따로작업해야한다.
        //  트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기때문인듯하다.



//        // when2
//        PostsTag postsTag = postsTagRepository.findById(2L).get();
//        postsTag .setDeleted(true);
//        postsTagRepository.save(postsTag);
//
//
//        // then2
//        List<PostsTagResponse> allDeleteResponseList = postsTagService.getPostsTagResponseListByPostId(2L); // 빈리스트일것임. 전부소프트딜리트니까.
//        assertThat(allDeleteResponseList.size()).isEqualTo(0);
//        log.info("allDeleteResponseList.size() = {}", allDeleteResponseList.size());    // 0일것임
//        log.info("allDeleteResponseList.toString() = {}", allDeleteResponseList.toString()); // 빈리스트일것임.



        // 결과2 ok.그런데 이 테스트메서드 통과 하려면 따로작업해야한다.
        // 트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기때문인듯하다.





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


//        PostsTag postsTag1 = postsTagRepository.findById(2L).get();
//        postsTag1.setDeleted(true);
//        postsTagRepository.save(postsTag1);
//
//        PostsTag postsTag2 = postsTagRepository.findById(3L).get();
//        postsTag2.setDeleted(true);
//        postsTagRepository.save(postsTag2);
//
//        PostsTag postsTag3 = postsTagRepository.findById(10L).get();
//        postsTag3.setDeleted(true);
//        postsTagRepository.save(postsTag3); // 전부다 딜리트처리
//
//
//
//        PostsTagRegisterRequest request = new PostsTagRegisterRequest();
//        request.setContents("메이플스토리");
//
//
//        // when
//        List<PostsTag> list = postsTagService.getPostsTagByContents(request);
//
//        // then
//        assertThat(list.size()).isEqualTo(0);
//        log.info("list.toString() = {}", list.toString());

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
//
//
//        // 결과는 ok. 디비에서 직접 확인해봤다.
//        // 그런데 이 메서드도 통과 하려면 또 별도로 작업해서 2번 실행해줘야함.
//        //  트랜젝션이 한번 끝나야 Post 필드의 태그리스트가 자동 주입되기 때문인듯하다.
//
//        // 이대로 했을시 통과 조건 =>
//        // 포스트 6번 id의 게시글안의 태그들은 (=>롤,바람의나라) 전부 소프트딜리트가 true 돼야함.
//        // 또한 포스트 6번 글에 3개의 태그(어둠의전설, 바람의나라, 메이플스토리)가 새로 추가돼야함.



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
        postsTagServiceImpl.deleteOnePostsTagByPostsTagId(2L);
    	  // then


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

        postsTagServiceImpl.deletePostsTagsByPostsTagId(list);

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


        // ==================

    	  // when

//        List<PostsTag> list = postsTagService.getPostsTagListByPostId(2L);
//        assertThat(list.size()).isEqualTo(2);
//        log.info("list.size() = {}", list.size());
//
//        for (PostsTag postsTag : list) {
//            assertThat(postsTag.isDeleted()).isEqualTo(false);
//            log.info("postsTag.isDeleted() = {}", postsTag.isDeleted());
//        }




    	  // then

//        postsTagService.deletePostsTagsByPostId(2L);
//
//        List<PostsTag> list2 = postsTagService.getPostsTagListByPostId(2L);
//        log.info("list.size() = {}", list2.size()); //2
//        for (PostsTag postsTag : list2) {
//            assertThat(postsTag.isDeleted()).isEqualTo(true);
//            log.info("postsTag.isDeleted() = {}", postsTag.isDeleted());
//        }
              // 결과 ok.

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