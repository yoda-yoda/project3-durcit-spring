package org.durcit.be.post.service.impl;

import org.durcit.be.post.domain.Like;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.LikeRepository;
import org.durcit.be.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.durcit.be.security.util.MockSecurityUtil.mockSecurityContext;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeServiceImplTest {

    @InjectMocks
    private LikeServiceImpl likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("게시글 좋아요 개수 조회 테스트 -> 성공")
    void shouldReturnLikeCount() {
        // given
        Long postId = 1L;
        long likeCount = 5L;
        when(likeRepository.countByPostId(postId)).thenReturn(likeCount);

        // when
        long result = likeService.getLikeCount(postId);

        // then
        assertEquals(likeCount, result);
        verify(likeRepository, times(1)).countByPostId(postId);
    }

    @Test
    @DisplayName("좋아요 추가 테스트")
    void shouldAddLike() {
        // given
        Long postId = 1L;
        Long memberId = 1L;
        Post post = Post.builder().id(postId).build();

        mockSecurityContext(memberId);
        when(postService.getById(postId)).thenReturn(post);
        when(likeRepository.findByPostIdAndMemberId(postId, memberId)).thenReturn(null);

        // when
        boolean result = likeService.toggleLike(postId);

        // then
        assertTrue(result);
        verify(postService, times(1)).getById(postId);
        verify(likeRepository, times(1)).findByPostIdAndMemberId(postId, memberId);
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void shouldRemoveLike() {
        // given
        Long postId = 1L;
        Long memberId = 1L;
        Like like = Like.builder().id(1L).build();

        mockSecurityContext(memberId);
        when(likeRepository.findByPostIdAndMemberId(postId, memberId)).thenReturn(like);

        // when
        boolean result = likeService.toggleLike(postId);

        // then
        assertFalse(result);
        verify(likeRepository, times(1)).findByPostIdAndMemberId(postId, memberId);
        verify(likeRepository, times(1)).delete(like);
    }

}