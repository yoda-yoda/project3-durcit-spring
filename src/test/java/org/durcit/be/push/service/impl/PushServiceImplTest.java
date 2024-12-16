package org.durcit.be.push.service.impl;

import org.durcit.be.push.domain.Push;
import org.durcit.be.push.domain.PushType;
import org.durcit.be.push.dto.PushResponse;
import org.durcit.be.push.repository.PushRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PushServiceImplTest {

    @Mock
    private PushRepository pushRepository;

    @InjectMocks
    private PushServiceImpl pushService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("푸시 알람 생성 테스트 -> 성공")
    void test_CreatePush() throws Exception {
        // given
        Push push = Push.builder()
                .memberId("memberId")
                .content("Test message")
                .pushType(PushType.FOLLOWER)
                .build();

        // when
        pushService.createPush(push);

        // then
        verify(pushRepository, times(1)).save(push);
    }

    @Test
    @DisplayName("푸시 알람 멤버 아이디 기준으로 조회 -> 성공")
    void test_GetPushs_ByMemberId() throws Exception {
        // given
        String memberId = "memberId";
        List<Push> pushList = Arrays.asList(
                Push.builder().content("Message 1").pushType(PushType.FOLLOWER).build(),
                Push.builder().content("Message 2").pushType(PushType.FOLLOWER).build()
        );
        when(pushRepository.findAllByMemberId(memberId)).thenReturn(pushList);

        // when
        List<PushResponse> result = pushService.getPushsByMemberId(memberId);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getMessage()).isEqualTo("Message 1");
        assertThat(result.get(1).getMessage()).isEqualTo("Message 2");

        verify(pushRepository, times(1)).findAllByMemberId(memberId);
    }



}