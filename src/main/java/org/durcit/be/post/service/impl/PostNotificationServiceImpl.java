package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.post.dto.PostNotificationMessage;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.service.PostNotificationService;
import org.durcit.be.push.domain.Push;
import org.durcit.be.push.domain.PushType;
import org.durcit.be.push.dto.NotificationMessage;
import org.durcit.be.push.service.PushService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostNotificationServiceImpl implements PostNotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final PushService pushService;

    public void notifyFollowers(PostResponse post, List<MemberFollowResponse> followers) {
        for (MemberFollowResponse follower : followers) {
            NotificationMessage notification = NotificationMessage.builder()
                    .messageReceiver(follower.getMemberId())
                    .postId(post.getId())
                    .message(post.getAuthor() + "님이 새 글을 작성하였습니다!")
                    .build();
            Push push = pushService.createPush(Push.builder()
                    .pushType(PushType.FOLLOWER)
                    .memberId(String.valueOf(follower.getMemberId()))
                    .content(notification.getMessage())
                    .postId(post.getId())
                    .build());
            notification.setId(push.getId());
            log.info("notification.getPostId() = {}", notification.getPostId());
            rabbitTemplate.convertAndSend("notifyExchange", "notify.notify", notification);
        }
    }
}
