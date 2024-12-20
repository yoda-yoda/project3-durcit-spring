package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.follow.service.MemberFollowService;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.dto.PostRegisterRequest;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.dto.PostUpdateRequest;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostNotificationService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.post.aop.annotations.PostRequireAuthorization;
import org.durcit.be.post.aop.annotations.RequireCurrentMemberId;
import org.durcit.be.system.exception.post.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.POST_NOT_FOUND_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MemberFollowService memberFollowService;
    private final PostNotificationService postNotificationService;

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .filter(post -> !post.isDeleted())
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long postId) {
        return postRepository.findById(postId)
                .filter(post -> !post.isDeleted())
                .map(PostResponse::fromEntity)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));
    }

    public Post getById(Long postId) {
        return postRepository.findById(postId)
                .filter(post -> !post.isDeleted())
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));
    }

    public Page<PostCardResponse> getPostsByPage(Pageable pageable, String category) {
        Sort sort = getSortByCategory(category);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return postRepository.findAll(sortedPageable)
                .map(PostCardResponse::fromEntity);
    }

    private Sort getSortByCategory(String category) {
        return switch (category.toLowerCase()) {
            case "best" -> Sort.by(Sort.Order.desc("views")); // 조회수 많은 순
            case "hot" -> Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("commentCount")); // 좋아요 + 댓글 많은 순
            default -> Sort.by(Sort.Order.desc("createdAt")); // 신규글 순
        };
    }

    @Transactional
    @RequireCurrentMemberId
    public PostResponse createPost(PostRegisterRequest postRegisterRequest) {
        Member member = memberService.getById(SecurityUtil.getCurrentMemberId());
        Post post = PostRegisterRequest.toEntity(postRegisterRequest);
        post.setMember(member);
        PostResponse postResponse = PostResponse.fromEntity(postRepository.save(post));

        List<MemberFollowResponse> followers = memberFollowService.getFollowers(member.getId());
        postNotificationService.notifyFollowers(postResponse, followers);

        return postResponse;
    }

    @Transactional
    @RequireCurrentMemberId
    @PostRequireAuthorization
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = getById(postId);
        post.updatePost(postUpdateRequest.getTitle(), postUpdateRequest.getContent());
        postRepository.save(post);
    }

    @Transactional
    @RequireCurrentMemberId
    @PostRequireAuthorization
    public void deletePost(Long postId) {
        Post post = getById(postId);
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Transactional
    public PostResponse getPostWithViewIncrement(Long postId) {
        Post post = postRepository.findById(postId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));
        postRepository.incrementViews(postId);
        return PostResponse.fromEntity(post);
    }

    public List<PostCardResponse> getMyPosts(Long memberId) {
        return postRepository.findByMemberId(memberId)
                .stream()
                .filter(post -> !post.isDeleted())
                .map(PostCardResponse::fromEntity)
                .collect(Collectors.toList());
    }


}
