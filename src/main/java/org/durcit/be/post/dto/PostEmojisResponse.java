package org.durcit.be.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostEmojisResponse {
    private Long postId;
    private List<EmojiDetails> emojis;
}