package org.durcit.be.chat.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.chat.dto.ChatMessageResponse;
import org.durcit.be.chat.dto.ChatRoomRequest;
import org.durcit.be.chat.dto.ChatRoomResponse;
import org.durcit.be.chat.service.ChatService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class ChatRestController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<ResponseData<ChatRoomResponse>> createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        ChatRoomResponse chatRoom = chatService.createChatRoom(chatRoomRequest);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_CHAT_ROOM_SUCCESS, chatRoom);
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseData<List<ChatRoomResponse>>> getMyChatRooms(@RequestParam Long memberId) {
        List<ChatRoomResponse> chatRooms = chatService.getChatRoomsByMemberId(memberId);
        return ResponseData.toResponseEntity(ResponseCode.GET_CHAT_ROOM_SUCCESS, chatRooms);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ResponseData<List<ChatMessageResponse>>> getMessagesByRoomId(@PathVariable Long roomId) {
        List<ChatMessageResponse> messages = chatService.getMessagesByRoomId(roomId);
        return ResponseData.toResponseEntity(ResponseCode.GET_CHAT_SUCCESS, messages);
    }

}
