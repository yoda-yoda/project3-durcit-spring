package org.durcit.be.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.log.domain.Log;
import org.durcit.be.log.repository.LogRepository;
import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.service.EmojiService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.durcit.be.system.service.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmojiController {

    private final EmojiService emojiService;
    private final WebSocketService webSocketService;
    private final LogRepository logRepository;

    @MessageMapping("/addEmoji")
    public void handleEmoji(@Payload EmojiRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String destination = headerAccessor.getDestination();
        log.info("WebSocket Message Received: sessionId={}, destination={}, payload={}", sessionId, destination, request);
        EmojiResponse emojiResponse = emojiService.toggleEmoji(request);
        String topic = String.format("/topic/emoji/%d", request.getPostId());
        webSocketService.sendMessageToTopic(topic, emojiResponse);

        Log log = Log.builder()
                .memberId(SecurityUtil.getCurrentMemberIdOrNull())
                .method("WebSocket")
                .endpoint(destination)
                .requestPayload(request.toString())
                .statusCode(200)
                .build();

        logRepository.save(log);
    }


}
