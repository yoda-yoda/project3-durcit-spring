package org.durcit.be.post.service.impl;

import org.durcit.be.post.domain.Emoji;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.EmojiRequest;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.dto.EmojiStatus;
import org.durcit.be.post.dto.EmojisMap;
import org.durcit.be.post.repository.EmojiRepository;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.util.MockSecurityUtil;
import org.durcit.be.security.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmojiServiceImplTest {
    @Mock
    private EmojiRepository emojiRepository;

    @Mock
    private PostService postService;

    @InjectMocks
    private EmojiServiceImpl emojiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("이모지 추가 테스트 -> 성공")
    void should_add_emoji() {
        // given
        Long postId = 1L;
        Long memberId = 2L;
        MockSecurityUtil.mockSecurityContext(memberId);
        String emojiSymbol = "👍";

        Post post = Post.builder()
                .id(postId)
                .build();

        EmojiRequest emojiRequest = new EmojiRequest(postId, emojiSymbol);

        when(postService.getById(postId)).thenReturn(post);
        when(emojiRepository.findByPostIdAndMemberIdAndEmoji(postId, memberId, emojiSymbol))
                .thenReturn(null);
        when(emojiRepository.aggregateEmojisByPostId(postId))
                .thenReturn(Map.of("👍", 1));

        // when
        EmojiResponse response = emojiService.toggleEmoji(emojiRequest);

        // then
        verify(emojiRepository, times(1)).save(any(Emoji.class));
        verify(emojiRepository, never()).delete(any(Emoji.class));

        assertThat(response.getAction()).isEqualTo(EmojiStatus.ADD.name());
        assertThat(response.getEmojis().getFirst()).isEqualTo(new EmojisMap("👍", 1));
    }

    @Test
    @DisplayName("이모지 삭제 테스트 -> 성공")
    void should_remove_emoji() {
        // given
        Long postId = 1L;
        Long memberId = 2L;
        MockSecurityUtil.mockSecurityContext(memberId);
        String emojiSymbol = "👍";

        Post post = Post.builder()
                .id(postId)
                .build();

        Emoji existingEmoji = Emoji.builder()
                .post(post)
                .emoji(emojiSymbol)
                .build();

        EmojiRequest emojiRequest = new EmojiRequest(postId, emojiSymbol);

        when(postService.getById(postId)).thenReturn(post);
        when(emojiRepository.findByPostIdAndMemberIdAndEmoji(postId, memberId, emojiSymbol))
                .thenReturn(existingEmoji);
        when(emojiRepository.aggregateEmojisByPostId(postId))
                .thenReturn(Map.of());

        // when
        EmojiResponse response = emojiService.toggleEmoji(emojiRequest);

        // then
        verify(emojiRepository, never()).save(any(Emoji.class));
        verify(emojiRepository, times(1)).delete(existingEmoji);

        assertThat(response.getAction()).isEqualTo(EmojiStatus.REMOVE.name());
    }


}