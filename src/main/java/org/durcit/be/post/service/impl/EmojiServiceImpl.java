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

import java.text.Normalizer;
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
        Member member = memberService.getById(emojiRequest.getMemberId());

        emojiRepository.deleteByPostIdAndMemberId(post.getId(), member.getId());
        log.info("Deleted existing emojis for postId: {}, memberId: {}", post.getId(), member.getId());

        Emoji newEmoji = Emoji.builder()
                .post(post)
                .member(member)
                .emoji(emojiRequest.getEmoji())
                .build();
        emojiRepository.save(newEmoji);
        log.info("Added new emoji: {} for postId: {}, memberId: {}", newEmoji.getEmoji(), post.getId(), member.getId());

        List<Emoji> emojis = emojiRepository.findByPostId(post.getId());

        Map<String, Integer> emojiCounts = new HashMap<>();
        for (Emoji emoji : emojis) {
            String normalizedEmoji = normalizeEmoji(emoji.getEmoji());
            emojiCounts.put(normalizedEmoji, emojiCounts.getOrDefault(normalizedEmoji, 0) + 1);
        }

        List<EmojisMap> emojiDetails = emojiCounts.entrySet().stream()
                .map(entry -> new EmojisMap(entry.getKey(), entry.getValue()))
                .toList();

        return new EmojiResponse(emojiRequest.getPostId(), emojiDetails, EmojiStatus.ADD.name());
    }

    private String normalizeEmoji(String emoji) {
        return Normalizer.normalize(emoji, Normalizer.Form.NFC);
    }

    public PostEmojisResponse getPostEmojis(Long postId) {
        List<Emoji> emojis = emojiRepository.findByPostId(postId);

        Map<String, Integer> emojiCounts = new HashMap<>();
        for (Emoji emoji : emojis) {
            String normalizedEmoji = normalizeEmoji(emoji.getEmoji());
            emojiCounts.put(normalizedEmoji, emojiCounts.getOrDefault(normalizedEmoji, 0) + 1);
        }

        List<EmojiDetails> emojiDetails = emojiCounts.entrySet().stream()
                .map(entry -> new EmojiDetails(entry.getKey(), entry.getValue(), false))
                .toList();
        return new PostEmojisResponse(postId, emojiDetails);
    }

}
