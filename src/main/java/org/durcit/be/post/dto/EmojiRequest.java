package org.durcit.be.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmojiRequest {

    private Long postId;
    private String emoji;
    private Long memberId;

}
