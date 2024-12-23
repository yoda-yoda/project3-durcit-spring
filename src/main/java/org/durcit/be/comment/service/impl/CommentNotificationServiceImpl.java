package org.durcit.be.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.comment.service.CommentNotificationService;
import org.durcit.be.push.domain.Push;
import org.durcit.be.push.domain.PushType;
import org.durcit.be.push.dto.NotificationMessage;
import org.durcit.be.push.service.PushService;
import org.durcit.be.security.domian.Member;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentNotificationServiceImpl implements CommentNotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final PushService pushService;

    public void notifyToTargetMember(Member member, Member targetMember, Long postId) {
        NotificationMessage notification = NotificationMessage.builder()
                .messageReceiver(targetMember.getId())
                .message(member.getNickname() + "님께서 작성하신 글에 댓글을 남겼습니다.")
                .confirmed(false)
                .build();

        Push push = pushService.createPush(Push.builder()
                .memberId(String.valueOf(targetMember.getId()))
                .content(notification.getMessage())
                .pushType(PushType.CHAT)
                .postId(postId)
                .build());

        notification.setId(push.getId());
        rabbitTemplate.convertAndSend("notifyExchange", "notify.notify", notification);
    }


}
