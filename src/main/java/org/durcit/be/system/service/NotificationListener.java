package org.durcit.be.system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.follow.dto.FollowNotificationMessage;
import org.durcit.be.post.dto.PostNotificationMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final WebSocketService webSocketService;

    @RabbitListener(queues = "postNotificationQueue")
    public void handleNotification(PostNotificationMessage message) {
        log.info("Received notification message: {}", message);
        webSocketService.sendMessageToUser(message.getFollowerId().toString(), "/queue/post/notifications", message.getMessage());
    }

    @RabbitListener(queues = "followNotificationQueue")
    public void handleFollowNotification(FollowNotificationMessage message) {
        log.info("Received notification message: {}", message);
        webSocketService.sendMessageToTopic("/topic/notification/" + message.getFolloweeId(), message);
    }

}
