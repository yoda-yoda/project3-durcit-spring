package org.durcit.be.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.chat.domain.ChatRoom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long roomId;
    private String nickname;
    private String targetNickname;
    private Long memberId;
    private Long targetId;
    private String memberProfile;
    private String targetProfile;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .nickname(chatRoom.getMember().getNickname())
                .targetNickname(chatRoom.getOpponent().getNickname())
                .memberId(chatRoom.getMember().getId())
                .targetId(chatRoom.getOpponent().getId())
                .memberProfile(chatRoom.getMember().getProfileImage())
                .targetProfile(chatRoom.getOpponent().getProfileImage())
                .build();
    }
}