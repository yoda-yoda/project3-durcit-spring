package org.durcit.be.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.dto.PostRegisterRequest;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.exception.post.PostNotFoundException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.POST_NOT_FOUND_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

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



}
