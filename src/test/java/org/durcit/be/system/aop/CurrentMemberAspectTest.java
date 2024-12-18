package org.durcit.be.system.aop;

import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.MockSecurityUtil;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class CurrentMemberAspectTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private TestService testService;

    @Test
    @DisplayName("유효한 유저로 접근 -> 성공")
    void should_pass_validation_when_member_exists() throws Exception {
        // given
        Long memberId = 1L;
        MockSecurityUtil.mockSecurityContext(memberId);
        when(memberService.isPresentById(memberId)).thenReturn(true);

        // when
        testService.sampleMethod();

        // then
        verify(memberService, times(1)).isPresentById(memberId);
    }

    @Test
    @DisplayName("유효하지 않은 유저로 접근 -> 실패")
    void should_throw_ex_when_member_does_not_exist() throws Exception {
        // given
        Long memberId = 1L;
        MockSecurityUtil.mockSecurityContext(memberId);
        when(memberService.isPresentById(memberId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> testService.sampleMethod())
                .isInstanceOf(MemberNotFoundException.class);

        verify(memberService, times(1)).isPresentById(memberId);
    }





}