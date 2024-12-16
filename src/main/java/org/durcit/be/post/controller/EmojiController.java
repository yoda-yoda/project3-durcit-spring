package org.durcit.be.post.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.service.EmojiService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.durcit.be.system.service.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EmojiController {

    private final EmojiService emojiService;
    private final WebSocketService webSocketService;

    @MessageMapping("/addEmoji")
    public void handleEmoji(@Payload EmojiRequest request) {
        EmojiResponse emojiResponse = emojiService.toggleEmoji(request);
        webSocketService.sendMessageToTopic("/topic/emoji", emojiResponse);
    }


}
