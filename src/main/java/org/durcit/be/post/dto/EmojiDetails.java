package org.durcit.be.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmojiDetails {
    private String emoji;
    private int count;
    private boolean isPressed;
}
