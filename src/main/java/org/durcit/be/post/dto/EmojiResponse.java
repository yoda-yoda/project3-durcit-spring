package org.durcit.be.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmojiResponse {

    private Long postId;
    private List<EmojisMap> emojis;
    private String action;

}
