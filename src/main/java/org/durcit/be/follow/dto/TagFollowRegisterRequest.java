package org.durcit.be.follow.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TagFollowRegisterRequest {


    @NotNull
    private String tag;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static TagFollow toEntity(TagFollowRegisterRequest tagFollowRegisterRequest, Member member) {

        TagFollow tagFollow = TagFollow.builder()
                .tag(tagFollowRegisterRequest.getTag())
                .member(member)
                .build();

        return tagFollow;
    }



}
