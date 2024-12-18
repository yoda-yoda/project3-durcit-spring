package org.durcit.be.post.service.impl;

import org.assertj.core.api.Assertions;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostRegisterRequest;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.dto.PostUpdateRequest;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.system.exception.auth.NoAuthenticationInSecurityContextException;
import org.durcit.be.system.exception.post.PostNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("전체 게시글 가져오기 -> 성공")
    void test_get_all_posts() throws Exception {
        // given
        Member member = Member.builder()
                .username("any")
                .email("any@any.com")
                .build();

        Post post1 = Post.builder()
                .title("Post 1")
                .member(member)
                .content("Post content 1")
                .build();

        Post post2 = Post.builder()
                .title("Post 2")
                .member(member)
                .content("Post content 2")
                .build();

        when(postRepository.findAll()).thenReturn(Arrays.asList(post1, post2));

        // when
        List<PostResponse> posts = postService.getAllPosts();

        // then
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts.get(0).getTitle()).isEqualTo("Post 1");
        assertThat(posts.get(1).getTitle()).isEqualTo("Post 2");
    }

    @Test
    @DisplayName("게시글 id로 게시글 가져오기 -> 성공")
    void test_get_post() throws Exception {
        // given
        Member member = Member.builder()
                .username("any")
                .email("any@any.com")
                .build();

        Post post = Post.builder()
                .title("Post 1")
                .member(member)
                .content("Post content 1")
                .views(0L)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        PostResponse postResponse = postService.getPostById(1L);

        // then
        assertThat(postResponse.getTitle()).isEqualTo("Post 1");
        assertThat(postResponse.getContent()).isEqualTo("Post content 1");
        assertThat(postResponse.getViews()).isEqualTo(0L);
    }

    @Test
    @DisplayName("게시글 id로 없는 게시글 가져오기 -> 실패")
    void test_get_post_by_id_postNotFound() throws Exception {
        // given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () -> postService.getPostById(1L));
    }

    public void mockSecurityContext(Long memberId) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(memberId);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getName()).thenReturn(memberId.toString());

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("게시글 생성 권한이 있는 유저로 게시글 생성 -> 성공")
    void test_create_post_with_auth_user() throws Exception {
        // given
        Long memberId = 1L;
        mockSecurityContext(memberId);

        Member member = Member.builder()
                .id(memberId)
                .build();

        PostRegisterRequest request = PostRegisterRequest.builder()
                .title("New Post")
                .content("New Content")
                .build();

        when(memberService.getById(1L)).thenReturn(member);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            savedPost.setId(1L);
            savedPost.setMember(member);
            return savedPost;
        });
        // when
        PostResponse postResponse = postService.createPost(request);

        // then
        assertThat(postResponse.getTitle()).isEqualTo("New Post");
        assertThat(postResponse.getContent()).isEqualTo("New Content");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저로 게시글 생성 -> 실패")
    void test_create_post_with_unauthorize_user() throws Exception {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .build();
        mockSecurityContext(memberId);
        PostRegisterRequest request = PostRegisterRequest.builder()
                .title("New Post")
                .content("New Content")
                .build();

        when(memberService.getById(1L)).thenReturn(member);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            savedPost.setId(1L);
            savedPost.setMember(member);
            return savedPost;
        });
        // when & then
        assertThrows(NoAuthenticationInSecurityContextException.class, () -> postService.createPost(request));
    }

    @Test
    @DisplayName("게시글 수정 권한이 있는 유저로 게시글 수정 -> 성공")
    void test_update_post_with_usable_update_user() throws Exception {
        // given
        Member member = Member.builder()
                .id(1L)
                .build();

        Post post = Post.builder()
                .title("Post 1")
                .content("Post content 1")
                .member(member)
                .build();

        PostUpdateRequest request = PostUpdateRequest.builder()
                .title("Post 2")
                .content("Post content 2")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        postService.updatePost(1L, request);

        // then
        verify(postRepository).save(post);
        Assertions.assertThat(post.getTitle()).isEqualTo("Post 2");
        Assertions.assertThat(post.getContent()).isEqualTo("Post content 2");
    }

    @Test
    @DisplayName("게시글 삭제 권한이 있는 사용자 게시글 삭제 -> 성공")
    void update_post_test_with_blank_request() throws Exception {
        // given
        Member member = Member.builder()
                .id(1L)
                .build();

        Post post = Post.builder()
                .title("Post 1")
                .content("Post content 1")
                .member(member)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        postService.deletePost(1L);

        // then
        verify(postRepository).save(post);
        assertThat(post.isDeleted()).isTrue();
    }


    @Test
    @DisplayName("게시물 조회 시 조회수 증가 테스트")
    void shouldIncrementViewsAndReturnPost() {
        // given
        Member member = Member.builder()
                .id(1L)
                .build();
        Long postId = 1L;
        Post post = Post.builder()
                .id(postId)
                .title("Post 1")
                .content("Post content 1")
                .member(member)
                .views(2L)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        doAnswer(invocation -> {
            post.setViews(post.getViews() + 1);
            return null;
        }).when(postRepository).incrementViews(postId);
        PostResponse response = postService.getPostWithViewIncrement(postId);

        // then
        verify(postRepository, times(1)).incrementViews(postId);
        assertThat(response.getViews()).isEqualTo(3L);
        assertThat(response.getTitle()).isEqualTo("Post 1");
        assertThat(response.getContent()).isEqualTo("Post content 1");
    }

    @Test
    @DisplayName("없는 게시물 조회 시 예외 발생 테스트")
    void shouldThrowExceptionWhenPostNotFound() {
        // given
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.getPostWithViewIncrement(postId))
                .isInstanceOf(PostNotFoundException.class);

        verify(postRepository, never()).incrementViews(postId);
    }
}