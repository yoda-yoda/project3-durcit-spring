package org.durcit.be.follow.repository;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@Transactional
@Rollback(false)
@SpringBootTest
class TagFollowRepositoryTest {

    @Autowired
    private TagFollowRepository tagFollowRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("리포지터리 findMemberByMemberId (jpql) 메서드 테스트 ")
    void a() throws Exception {
        // given

        Member mem1 = Member.builder()
                .nickname("멤버1")
                .email("abc@naver.com")
                .build();

        memberRepository.save(mem1);

        // when
        Member find1 = tagFollowRepository.findMemberByMemberId(1L);

        // then
        assertThat(find1.getNickname()).isEqualTo("멤버1");



        // 없는멤버라 오류 //
//        Member find2 = tagFollowRepository.findMemberByMemberId(2L);
//        find2.getNickname();

    }







}