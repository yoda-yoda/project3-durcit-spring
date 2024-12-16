package org.durcit.be.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.dto.NotificationMessage;
import org.durcit.be.system.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final WebSocketService webSocketService;

    @RabbitListener(queues = "postNotificationQueue")
    public void handleNotification(NotificationMessage message) {
        log.info("Received notification message: {}", message);
        webSocketService.sendMessageToUser(message.getFollowerId().toString(), "/queue/post/notifications", message.getMessage());
    }

}
