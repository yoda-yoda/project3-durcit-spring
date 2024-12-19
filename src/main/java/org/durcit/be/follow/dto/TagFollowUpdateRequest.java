package org.durcit.be.follow.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.security.domian.Member;
import java.time.LocalDateTime;

@Getter
public class TagFollowUpdateRequest {

    @NotNull
    private String tag;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static TagFollow toEntity(TagFollowUpdateRequest tagFollowUpdateRequest, Member member) {

        TagFollow tagFollow = TagFollow.builder()
                .tag(tagFollowUpdateRequest.getTag())
                .member(member)
                .build();

        return tagFollow;
    }

}
