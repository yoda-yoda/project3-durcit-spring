package org.durcit.be.chat.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.chat.dto.ChatMessageRequest;
import org.durcit.be.chat.dto.ChatMessageResponse;
import org.durcit.be.chat.service.ChatService;
import org.durcit.be.system.service.WebSocketService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final WebSocketService webSocketService;



    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageRequest messageRequest) {
        ChatMessageResponse messageResponse = chatService.processMessage(messageRequest);

        webSocketService.sendMessageToTopic(
                "/topic/chat/" + messageResponse.getRoomId(),
                messageResponse
        );
    }

}
