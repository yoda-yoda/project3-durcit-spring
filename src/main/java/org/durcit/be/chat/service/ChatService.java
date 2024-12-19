package org.durcit.be.chat.service;

import org.durcit.be.chat.dto.ChatMessageRequest;
import org.durcit.be.chat.dto.ChatMessageResponse;
import org.durcit.be.chat.dto.ChatRoomRequest;
import org.durcit.be.chat.dto.ChatRoomResponse;

import java.util.List;

public interface ChatService {

    public ChatMessageResponse processMessage(ChatMessageRequest messageRequest);
    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest);
    public List<ChatRoomResponse> getChatRoomsByMemberId(Long memberId);

}
