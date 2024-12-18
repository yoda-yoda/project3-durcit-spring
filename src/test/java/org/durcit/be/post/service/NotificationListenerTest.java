package org.durcit.be.post.service;

import org.durcit.be.post.dto.NotificationMessage;
import org.durcit.be.system.service.WebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationListenerTest {

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private NotificationListener notificationListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("RabbitMQ 메시지 수신 -> WebSocket 메시지 전송")
    void handleNotification_shouldSendMessageToWebSocket() {
        // given
        NotificationMessage message = NotificationMessage.builder()
                .followerId(1L)
                .postId(10L)
                .message("새 글이 작성되었습니다!")
                .build();

        // when
        notificationListener.handleNotification(message);

        // then
        verify(webSocketService, times(1))
                .sendMessageToUser(eq("1"), eq("/queue/post/notifications"), eq("새 글이 작성되었습니다!"));
    }

}