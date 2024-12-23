package org.durcit.be.comment.service;

import org.durcit.be.comment.dto.CommentCardResponse;
import org.durcit.be.comment.dto.CommentRegisterRequest;
import org.durcit.be.comment.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {
    public List<CommentCardResponse> getCommentsByPostId(Long postId);
    public List<CommentCardResponse> getCommentsByMemberId(Long memberId);
    public void registerComment(CommentRegisterRequest request);
    public void updateComment(CommentUpdateRequest request);
    public void deleteComment(Long commentId);
    public List<CommentCardResponse> getDeletedComments();
    public void restoreDeletedComments(Long commentId);
    public void restoreCommentsByPostId(Long postId);
}
