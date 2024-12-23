package org.durcit.be.system.service.impl;

import org.durcit.be.system.service.WebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

class WebSocketServiceImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketServiceImpl webSocketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    void testSendMessageToUser() {
        // Given
        String userId = "1";
        String destination = "/queue/messages";
        String message = "Hello, User!";

        // When
        webSocketService.sendMessageToUser(userId, destination, message);

        // Then
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(userId, destination, message);
    }

    @Test
    void testSendMessageToTopic() {
        // Given
        String topic = "/topic/updates";
        String message = "Broadcast message!";

        // When
        webSocketService.sendMessageToTopic(topic, message);

        // Then
        verify(messagingTemplate, times(1))
                .convertAndSend(topic, message);
    }
}