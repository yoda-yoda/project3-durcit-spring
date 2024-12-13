package org.durcit.be.post.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.service.EmojiService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/emojis")
public class EmojiController {

    private final EmojiService emojiService;

    @PostMapping
    public ResponseEntity<ResponseData<EmojiResponse>> toggleEmoji(@RequestBody EmojiRequest emojiRequest) {
        return ResponseData.toResponseEntity(ResponseCode.TOGGLE_EMOJI_SUCCESS, emojiService.toggleEmoji(emojiRequest));
    }


}
