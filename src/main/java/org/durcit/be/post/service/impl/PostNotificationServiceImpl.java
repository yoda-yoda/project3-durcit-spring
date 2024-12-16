package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.NotificationMessage;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.service.PostNotificationService;
import org.durcit.be.security.domian.Member;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostNotificationServiceImpl implements PostNotificationService {

    private final RabbitTemplate rabbitTemplate;

    public void notifyFollowers(PostResponse post, List<MemberFollowResponse> followers) {
        for (MemberFollowResponse follower : followers) {
            NotificationMessage message = NotificationMessage.builder()
                    .followerId(follower.getMemberId())
                    .postId(post.getId())
                    .message(post.getAuthor() + "님이 새 글을 작성하였습니다!")
                    .build();
            rabbitTemplate.convertAndSend("postExchange", "post.notify", message);
        }
    }
}
