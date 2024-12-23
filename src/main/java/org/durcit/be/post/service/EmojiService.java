package org.durcit.be.post.service;

import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.dto.PostEmojisResponse;

public interface EmojiService {

    public EmojiResponse toggleEmoji(EmojiRequest emojiRequest);
    public PostEmojisResponse getPostEmojis(Long postId);

}
