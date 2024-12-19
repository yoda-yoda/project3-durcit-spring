package org.durcit.be.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.chat.domain.ChatMessage;
import org.durcit.be.chat.domain.ChatRoom;
import org.durcit.be.chat.dto.ChatMessageRequest;
import org.durcit.be.chat.dto.ChatMessageResponse;
import org.durcit.be.chat.dto.ChatRoomRequest;
import org.durcit.be.chat.dto.ChatRoomResponse;
import org.durcit.be.chat.repository.ChatMessageRepository;
import org.durcit.be.chat.repository.ChatRoomRepository;
import org.durcit.be.chat.service.ChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatRoomResponse> getChatRoomsByMemberId(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMemberId(memberId);
        return chatRooms.stream()
                .map(ChatRoomResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest) {
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByMemberIds(
                chatRoomRequest.getMemberId(),
                chatRoomRequest.getTargetMemberId()
        );

        if (existingRoom.isPresent()) {
            return ChatRoomResponse.fromEntity(existingRoom.get());
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .userId(chatRoomRequest.getMemberId())
                .opponentId(chatRoomRequest.getTargetMemberId())
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.fromEntity(savedRoom);
    }

    @Transactional
    public ChatMessageResponse processMessage(ChatMessageRequest messageRequest) {
        ChatRoom chatRoom = chatRoomRepository
                .findByUserIdAndOpponentId(messageRequest.getSenderId(), messageRequest.getOpponentId())
                .or(() -> chatRoomRepository.findByUserIdAndOpponentId(messageRequest.getOpponentId(), messageRequest.getSenderId()))
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.create(messageRequest.getSenderId(), messageRequest.getOpponentId())));


        ChatMessage chatMessage = ChatMessage.create(chatRoom, messageRequest.getSenderId(), messageRequest.getMessage());
        chatMessageRepository.save(chatMessage);

        return ChatMessageResponse.builder()
                .roomId(chatRoom.getId())
                .senderId(chatMessage.getSenderId())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

}
