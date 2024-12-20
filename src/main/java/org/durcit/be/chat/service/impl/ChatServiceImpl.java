package org.durcit.be.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.chat.domain.ChatMessage;
import org.durcit.be.chat.domain.ChatRoom;
import org.durcit.be.chat.dto.ChatMessageRequest;
import org.durcit.be.chat.dto.ChatMessageResponse;
import org.durcit.be.chat.dto.ChatRoomRequest;
import org.durcit.be.chat.dto.ChatRoomResponse;
import org.durcit.be.chat.repository.ChatMessageRepository;
import org.durcit.be.chat.repository.ChatRoomRepository;
import org.durcit.be.chat.service.ChatService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.system.exception.chat.InvalidChatRoomCreationException;
import org.durcit.be.system.exception.chat.InvalidChatRoomIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.INVALID_CHAT_ROOM_CREATION_ERROR;
import static org.durcit.be.system.exception.ExceptionMessage.INVALID_CHAT_ROOM_ID_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;

    public List<ChatRoomResponse> getChatRoomsByMemberId(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberId(memberId);
        return chatRooms.stream()
                .map(ChatRoomResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest) {
        if (Objects.equals(chatRoomRequest.getMemberId(), chatRoomRequest.getTargetMemberId())) {
            throw new InvalidChatRoomCreationException(INVALID_CHAT_ROOM_CREATION_ERROR);
        }

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByMemberIds(
                chatRoomRequest.getMemberId(),
                chatRoomRequest.getTargetMemberId()
        );

        if (existingRoom.isPresent()) {
            return ChatRoomResponse.fromEntity(existingRoom.get());
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .member(memberService.getById(chatRoomRequest.getMemberId()))
                .opponent(memberService.getById(chatRoomRequest.getTargetMemberId()))
                .build();
        log.info("chatRoom.getOpponent().getRole() = {}", chatRoom.getOpponent().getId());
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.fromEntity(savedRoom);
    }

    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(roomId);

        if (messages.isEmpty()) {
            return new ArrayList<>();
        }

        return messages.stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageResponse processMessage(ChatMessageRequest messageRequest) {
        log.info("messageRequest.getTargetNickname() = {}", messageRequest.getTargetNickname());
        Member targetMember = memberService.getByNickname(messageRequest.getTargetNickname());
        log.info("targetMember = {}", targetMember.getId());
        ChatRoom chatRoom = chatRoomRepository
                .findByMemberIds(messageRequest.getSenderId(), targetMember.getId())
                .or(() -> chatRoomRepository.findByMemberIds(targetMember.getId(), messageRequest.getSenderId()))
                .orElseGet(() -> {
                    if (Objects.equals(messageRequest.getSenderId(), targetMember.getId())) {
                        throw new InvalidChatRoomCreationException(INVALID_CHAT_ROOM_CREATION_ERROR);
                    }
                    return chatRoomRepository.save(ChatRoom.create(
                            memberService.getById(messageRequest.getSenderId()),
                            memberService.getById(targetMember.getId())
                    ));
                });


        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(memberService.getById(messageRequest.getSenderId()))
                .content(messageRequest.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);

        return ChatMessageResponse.builder()
                .roomId(chatRoom.getId())
                .senderId(chatMessage.getSender().getId())
                .targetId(targetMember.getId())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

}
