package org.durcit.be.follow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.follow.dto.FollowNotificationMessage;
import org.durcit.be.follow.service.FollowNotificationService;
import org.durcit.be.push.domain.Push;
import org.durcit.be.push.domain.PushType;
import org.durcit.be.push.service.PushService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.system.service.WebSocketService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FollowNotificationServiceImpl implements FollowNotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final PushService pushService;

    public void notifyToFollowee(Member follower, Long followeeId) {
        FollowNotificationMessage notification = FollowNotificationMessage.builder()
                .followerId(follower.getId())
                .followerNickname(follower.getNickname())
                .followeeId(followeeId)
                .message(follower.getNickname() + "님이 팔로우를 시작하셨습니다!")
                .build();
        log.info("follower = {}", follower);
        log.info("followeeId = {}", followeeId);
        pushService.createPush(Push.builder()
                .pushType(PushType.NEW_FOLLOWER)
                .memberId(String.valueOf(followeeId))
                .content(notification.getMessage())
                .build());
        rabbitTemplate.convertAndSend("followExchange", "follow.notify", notification);
    }



}
