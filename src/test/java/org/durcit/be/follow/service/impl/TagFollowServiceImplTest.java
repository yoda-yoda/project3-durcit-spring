package org.durcit.be.follow.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.follow.dto.TagFollowRegisterRequest;
import org.durcit.be.follow.dto.TagFollowResponse;
import org.durcit.be.follow.repository.TagFollowRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@Transactional
@Rollback(false)
@SpringBootTest
class TagFollowServiceImplTest {


    @Autowired
    private TagFollowRepository tagFollowRepository;
    @Autowired
    private TagFollowServiceImpl tagFollowServiceImpl;
    @Autowired
    private MemberRepository memberRepository;


    
    @Test
    @DisplayName("createAndDeleteTagFollowByRegisterRequest 메서드 테스트")
    void createAndDeleteTagFollowByRegisterRequest_method_test() throws Exception {


    		// given
        Member mem1 = Member.builder()
                .nickname("멤버1")
                .email("aaa@naver.com")
                .build();

        Member mem2 = Member.builder()
                .nickname("멤버2")
                .email("bbb@naver.com")
                .build();

        memberRepository.save(mem1);
        memberRepository.save(mem2);

        TagFollow follow1 = TagFollow.builder()
                .tag("메이플스토리")
                .member(mem1)
                .build();

        tagFollowRepository.save(follow1);



//        // when
//        TagFollowRegisterRequest req = new TagFollowRegisterRequest();
//        req.setTag("메이플스토리");
//
//        TagFollowResponse tagFollowResponse = tagFollowServiceImpl.createAndDeleteTagFollowByRegisterRequest(req, 1L);
//
//        // then
//        assertThat(tagFollowResponse.getTag()).isEqualTo("메이플스토리");
//        log.info("tagFollowResponse.getTag() = {}", tagFollowResponse.getTag());
//
//        // 결과 1 정상작동 OK => DB에서 직접확인함. 유저가 요청 태그를 디비에 갖고있을때 true 면 false 되고, flase면 true 된다.
//        // 결과 2 정상작동 OK => DB에 아예 없는 상태에서도 생성되고, 또 반복적으로 결과1처럼 작동한다.
//        // 응답 객체도 잘 만들어진다.


    }
    


    // OK
    @Test
    @DisplayName("getTagFollows 메서드 테스트")
    void getTagFollows_method_test() throws Exception {


    		// given


        Member mem1 = Member.builder()
                .nickname("멤버1")
                .email("aaa@naver.com")
                .build();

        Member mem2 = Member.builder()
                .nickname("멤버2")
                .email("bbb@naver.com")
                .build();

        memberRepository.save(mem1);
        memberRepository.save(mem2);


        Member findMem1 = memberRepository.findById(1L).get();

        TagFollow follow1 = TagFollow.builder()
                .tag("메이플스토리")
                .member(findMem1)
                .build();

        tagFollowRepository.save(follow1);





        // when
//         List<TagFollow> tagFollows = tagFollowServiceImpl.getTagFollows(1L);
//
//        // then
//        assertThat(tagFollows.size()).isEqualTo(1);
//        String tag = tagFollows.get(0).getTag();
//        log.info("tag = {}", tag);
//
//
//        TagFollow tagFollow = tagFollowRepository.findById(1L).get();
//        tagFollow.setDeleted(true);
//        tagFollowRepository.save(tagFollow);
//
//        List<TagFollow> tagFollows = tagFollowServiceImpl.getTagFollows(1L);

        // 결과1 ok: 해당 멤버가 태그 없으면 오류 잘작동.
        // 결과2 ok: 해당 멤버가 태그있으면 정상적 잘 작동.
        // 결과3 ok: 해당 멤버가 디비에 가진 1개의 태그가 delete 되어있으면 오류 잘작동.

    }


    @Test
    @DisplayName("멤버가 태그 안가지고를 있을때 getTagFollowsResponses 메서드 테스트")
    void getTagFollowsResponses_method_test1() throws Exception {

    		// given

//        Member mem1 = Member.builder()
//                .nickname("멤버1")
//                .email("aaa@naver.com")
//                .build();
//
//        memberRepository.save(mem1);


    	  // when

        List<TagFollowResponse> tagFollowsResponse = tagFollowServiceImpl.getTagFollowsResponses(1L);
        assertThat(tagFollowsResponse.size()).isEqualTo(0);
        assertThat(tagFollowsResponse.isEmpty()).isEqualTo(true);
        log.info("tagFollowsResponse.toString() = {}", tagFollowsResponse.toString());

        // then

        // 결과 ok:  오류안나고, 빈 리스트를 반환해야 정상. 트랜젝션 작업해야함.

    }



    @Test
    @DisplayName("멤버가 태그 가지고 있는데 deleted 된거일때 getTagFollowsResponses 메서드 테스트")
    void getTagFollowsResponses_method_test2() throws Exception {

        // given

//        Member mem1 = Member.builder()
//                .nickname("멤버1")
//                .email("aaa@naver.com")
//                .build();
//
//        memberRepository.save(mem1);
//
//
//        TagFollow follow1 = TagFollow.builder()
//                .tag("메이플스토리")
//                .member(mem1)
//                .build();
//
//        tagFollowRepository.save(follow1);

//        TagFollow tagFollow = tagFollowRepository.findById(1).get();
//        tagFollow.setDeleted(true);
//        tagFollowRepository.save(tagFollow);


        // when

        List<TagFollowResponse> tagFollowsResponse = tagFollowServiceImpl.getTagFollowsResponses(1L);


        // then

        assertThat(tagFollowsResponse.size()).isEqualTo(0);
        assertThat(tagFollowsResponse.isEmpty()).isEqualTo(true);

        // 결과 ok:  오류안나고, 빈 리스트 반환하고 isEmpty가 true여야 정상.

    }



    @Test
    @DisplayName("멤버가 delete 안된 태그 가지고있을때 getTagFollowsResponses 메서드 테스트")
    void getTagFollowsResponses_method_test3() throws Exception {
            // given

          // when

          // then

    //    assertThat(tagFollowsResponse.size()).isEqualTo(1);
    //
    //    TagFollowResponse tagFollowResponse = tagFollowsResponse.get(0);
    //
    //    assertThat(tagFollowResponse.getTag()).isEqualTo("메이플스토리");
    //
    //
    //    log.info("tagFollowsResponse.toString() = {}", tagFollowsResponse.toString());
    }












}