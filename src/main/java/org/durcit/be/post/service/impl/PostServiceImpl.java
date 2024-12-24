package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.follow.dto.MemberFollowResponse;
import org.durcit.be.follow.dto.TagFollowMembersResponse;
import org.durcit.be.follow.repository.TagFollowRepository;
import org.durcit.be.follow.service.MemberFollowService;
import org.durcit.be.follow.service.TagFollowService;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.post.dto.PostRegisterRequest;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.dto.PostUpdateRequest;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostNotificationService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.post.aop.annotations.PostRequireAuthorization;
import org.durcit.be.post.aop.annotations.RequireCurrentMemberId;
import org.durcit.be.system.exception.post.PostNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final TagFollowRepository tagFollowRepository;
    private final TagFollowService tagFollowService;

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .filter(post -> !post.isDeleted())
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementPostViews(Long postId) {
        postRepository.incrementViews(postId);
    }

    @Transactional
    public PostResponse getPostById(Long postId) {
        incrementPostViews(postId);
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

        Page<Post> posts = postRepository.findAll(sortedPageable);

        if (category.equalsIgnoreCase("hot")) {
            return getSortByHotCategory(posts, sortedPageable);
        }

        return posts.map(PostCardResponse::fromEntity);
    }



    public Page<PostCardResponse> getPostsByFollowedTags(Long memberId, PageRequest pageRequest, String category) {
        List<TagFollow> followedTags = tagFollowRepository.findTagsByMemberId(memberId);
        if (followedTags.isEmpty()) {
            return Page.empty();
        }
        List<String> followedTagsString = followedTags.stream().map(TagFollow::getTag).toList();

        Sort sort = getSortByCategory(category);
        PageRequest sortedPageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        Page<Post> posts = postRepository.findPostsByTags(followedTagsString, sortedPageRequest);

        if (category.equalsIgnoreCase("hot")) {
            return getSortByHotCategory(posts, sortedPageRequest);
        }

        return posts.map(PostCardResponse::fromEntity);
    }

    private PageImpl<PostCardResponse> getSortByHotCategory(Page<Post> posts, Pageable pageRequest) {
        List<PostCardResponse> sortedPosts = posts.stream()
                .map(PostCardResponse::fromEntity)
                .sorted(Comparator.comparingLong((PostCardResponse p) -> p.getLikeCount() + p.getCommentCount())
                        .reversed())
                .collect(Collectors.toList());

        return new PageImpl<>(sortedPosts, pageRequest, sortedPosts.size());
    }

    private Sort getSortByCategory(String category) {
        return switch (category.toLowerCase()) {
            case "best" -> Sort.by(Sort.Order.desc("views")); // 조회수 많은 순
            case "hot" -> Sort.unsorted(); // DTO 변환 후 정렬
            default -> Sort.by(Sort.Order.desc("createdAt")); // 신규글 순
        };
    }

    @Transactional(readOnly = true)
    public Page<PostCardResponse> searchPostsByTag(String tag, PageRequest pageRequest) {
        return postRepository.findPostsByTag(tag, pageRequest)
                .map(PostCardResponse::fromEntity);
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

        // List<PostsTag> -> List<String>
        List<TagFollowMembersResponse> tagFollowers = tagFollowService.getTagFollowMembersResponses(
                post.getPostsTagList().stream()
                        .map(PostsTag::getContents)
                        .toList()
        );

        List<TagFollowMembersResponse> list = tagFollowers.stream().peek(tagFollowMembersResponse -> tagFollowMembersResponse.setPostId(post.getId())).toList();

        postNotificationService.notifyTagFollowers(list);


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




    // 메서드 기능: 해당 게시글의 delete 를 false로 바꾼다.
    // 예외: 해당 포스트를 찾지못하면 예외를 던진다.
    // 반환: 저장한 Post를 반환한다.
    @Transactional
    public Post recoverPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));

        post.setDeleted(false);
        return postRepository.save(post);

    }




}
