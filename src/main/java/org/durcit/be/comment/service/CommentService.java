package org.durcit.be.comment.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.comment.domain.PostComment;
import org.durcit.be.comment.repository.PostCommentRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final PostCommentRepository postCommentRepository;

    @Transactional
    public PostComment save(PostComment postComment) {
        return postCommentRepository.save(postComment);
    }

    public PostComment findById(Long id) {
        return postCommentRepository.findById()
                .filter(tag -> !tag.isDeleted())
    }




}
