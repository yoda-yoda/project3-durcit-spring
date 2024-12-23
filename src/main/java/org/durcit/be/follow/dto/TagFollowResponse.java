package org.durcit.be.follow.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.durcit.be.follow.domain.TagFollow;

import java.time.LocalDateTime;

@Setter
@Getter
public class TagFollowResponse {

    private Long id;
    private String tag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TagFollowResponse fromEntity(TagFollow tagFollow) {

        TagFollowResponse buildResponse = TagFollowResponse.builder()
                .id(tagFollow.getId())
                .tag(tagFollow.getTag())
                .createdAt(tagFollow.getCreatedAt())
                .updatedAt(tagFollow.getUpdatedAt())
                .build();

        return buildResponse;
    }

    @Builder
    public TagFollowResponse(Long id, String tag, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.tag = tag;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

}
