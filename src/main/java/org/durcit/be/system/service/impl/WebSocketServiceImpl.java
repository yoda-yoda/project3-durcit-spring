package org.durcit.be.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.system.service.WebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessageToUser(String userId, String destination, Object message) {
        log.info("Sending message to user {}: {}", userId, message);
        messagingTemplate.convertAndSendToUser(userId, destination, message);
    }

    public void sendMessageToTopic(String topic, Object message) {
        log.info("Sending message to topic {}: {}", topic, message);
        messagingTemplate.convertAndSend(topic, message);
    }
}
