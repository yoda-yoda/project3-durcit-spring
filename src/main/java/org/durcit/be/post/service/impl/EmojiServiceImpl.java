package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Emoji;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.dto.EmojiStatus;
import org.durcit.be.post.dto.EmojisMap;
import org.durcit.be.post.repository.EmojiRepository;
import org.durcit.be.post.service.EmojiService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EmojiServiceImpl implements EmojiService {

    private final EmojiRepository emojiRepository;
    private final PostService postService;

    @Transactional
    public EmojiResponse toggleEmoji(EmojiRequest emojiRequest) {
        Post post = postService.getById(emojiRequest.getPostId());
        Long memberId = SecurityUtil.getCurrentMemberId();
        Emoji existingEmoji = emojiRepository.findByPostIdAndMemberIdAndEmoji(post.getId(), memberId, emojiRequest.getEmoji());

        String action;
        if (existingEmoji == null) {
            emojiRepository.save(Emoji.builder()
                    .post(post)
                    .member(Member.builder().id(memberId).build())
                    .emoji(emojiRequest.getEmoji())
                    .build());
            action = EmojiStatus.ADD.name();
        } else {
            emojiRepository.delete(existingEmoji);
            action = EmojiStatus.REMOVE.name();
        }

        List<EmojisMap> emojiList = emojiRepository.aggregateEmojisByPostId(emojiRequest.getPostId())
                .entrySet()
                .stream()
                .map(entry -> new EmojisMap(entry.getKey(), entry.getValue()))
                .toList();
        return new EmojiResponse(emojiRequest.getPostId(), emojiList, action);
    }

}
