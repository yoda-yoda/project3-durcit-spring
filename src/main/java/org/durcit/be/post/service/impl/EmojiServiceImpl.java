package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Emoji;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.*;
import org.durcit.be.post.repository.EmojiRepository;
import org.durcit.be.post.service.EmojiService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EmojiServiceImpl implements EmojiService {

    private final EmojiRepository emojiRepository;
    private final PostService postService;
    private final MemberService memberService;

    @Transactional
    public EmojiResponse toggleEmoji(EmojiRequest emojiRequest) {
        Post post = postService.getById(emojiRequest.getPostId());

        emojiRepository.deleteByPostIdAndMemberId(post.getId(), emojiRequest.getMemberId());

        emojiRepository.save(Emoji.builder()
                .post(post)
                .member(memberService.getById(emojiRequest.getMemberId()))
                .emoji(emojiRequest.getEmoji())
                .build());

        List<Object[]> results = emojiRepository.aggregateEmojisByPostId(emojiRequest.getPostId());

        List<EmojisMap> emojiCounts = new ArrayList<>();
        for (Object[] result : results) {
            String emoji = (String) result[0];
            Integer count = ((Number) result[1]).intValue();
            emojiCounts.add(new EmojisMap(emoji, count));
        }

        return new EmojiResponse(emojiRequest.getPostId(), emojiCounts, EmojiStatus.ADD.name());
    }

    public PostEmojisResponse getPostEmojis(Long postId) {
        List<Object[]> results = emojiRepository.aggregateEmojisByPostId(postId);

        List<EmojiDetails> emojiCounts = new ArrayList<>();
        for (Object[] result : results) {
            String emoji = (String) result[0];
            Integer count = ((Number) result[1]).intValue();
            emojiCounts.add(new EmojiDetails(emoji, count, false));
        }
        return new PostEmojisResponse(postId, emojiCounts);
    }

}
