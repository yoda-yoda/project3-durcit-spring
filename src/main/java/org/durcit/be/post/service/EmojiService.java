package org.durcit.be.post.service;

import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;

public interface EmojiService {

    public EmojiResponse toggleEmoji(EmojiRequest emojiRequest);

}
