package org.durcit.be.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.chat.service.ChatNotificationService;
import org.durcit.be.push.domain.Push;
import org.durcit.be.push.domain.PushType;
import org.durcit.be.push.dto.NotificationMessage;
import org.durcit.be.push.service.PushService;
import org.durcit.be.security.domian.Member;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatNotificationServiceImpl implements ChatNotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final PushService pushService;

    public void notifyToTargetMember(Member member, Member targetMember) {
        NotificationMessage notification = NotificationMessage.builder()
                .messageReceiver(targetMember.getId())
                .message(member.getNickname() + "님께서 채팅을 보내셨습니다!")
                .confirmed(false)
                .build();

        Push push = pushService.createPush(Push.builder()
                .memberId(String.valueOf(targetMember.getId()))
                .content(notification.getMessage())
                .pushType(PushType.CHAT)
                .build());

        notification.setId(push.getId());
        rabbitTemplate.convertAndSend("notifyExchange", "notify.notify", notification);
    }

}
