package org.durcit.be.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagFollowMembersResponse {

    private String tag;
    private Long memberId;
    private Long postId;


    public TagFollowMembersResponse(String tag, Long memberId) {
            this.tag = tag;
            this.memberId = memberId;
    }


}



