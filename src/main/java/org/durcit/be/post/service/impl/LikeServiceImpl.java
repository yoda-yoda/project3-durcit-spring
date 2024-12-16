package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Like;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.LikeRepository;
import org.durcit.be.post.service.LikeService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {

    private final PostService postService;
    private final LikeRepository likeRepository;

    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Transactional
    public boolean toggleLike(Long postId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = Member.builder().id(memberId).build();
        Post post = postService.getById(postId);

        Like existingLike = likeRepository.findByPostIdAndMemberId(postId, memberId);
        if (existingLike != null) {
            likeRepository.delete(existingLike);
            return false;
        } else {
            Like newLike = Like.builder()
                    .post(post)
                    .member(member)
                    .build();
            likeRepository.save(newLike);
            return true;
        }
    }
}
