package org.durcit.be.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.comment.domain.Comment;
import org.durcit.be.comment.domain.CommentMention;
import org.durcit.be.comment.dto.CommentCardResponse;
import org.durcit.be.comment.dto.CommentRegisterRequest;
import org.durcit.be.comment.dto.CommentUpdateRequest;
import org.durcit.be.comment.repository.CommentMentionRepository;
import org.durcit.be.comment.repository.CommentRepository;
import org.durcit.be.comment.service.CommentService;
import org.durcit.be.comment.service.MentionNotificationService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.durcit.be.system.exception.auth.UnauthorizedAccessException;
import org.durcit.be.system.exception.comment.InvalidCommentIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.durcit.be.system.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMentionRepository commentMentionRepository;
    private final MemberService memberService;
    private final MentionNotificationService mentionNotificationService;
    private final PostService postService;

    public List<CommentCardResponse> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdAndDeletedFalse(postId);
        return comments.stream().map(CommentCardResponse::from).toList();
    }

    public List<CommentCardResponse> getCommentsByMemberId(Long memberId) {
        List<Comment> comments = commentRepository.findAllByAuthorIdAndDeletedFalse(memberId);
        return comments.stream().map(CommentCardResponse::from).toList();
    }

    @Transactional
    public void registerComment(CommentRegisterRequest request) {
        log.info("request.getPostId() = {}", request.getPostId());
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId()).orElseThrow(
                    () -> new InvalidCommentIdException(INVALID_COMMENT_ID_ERROR)
            );
        }
        Comment comment = Comment.builder()
                .parent(parentComment)
                .content(request.getContent())
                .author(memberService.getById(SecurityUtil.getCurrentMemberId()))
                .post(postService.getById(request.getPostId()))
                .build();
        commentRepository.save(comment);

        if (request.getMentionList() != null && !request.getMentionList().isEmpty()) {
            request.getMentionList().forEach(nickname -> {
                Member targetMember = memberService.findByNickname(nickname);
                if (targetMember == null) {
                    throw new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR);
                }

                CommentMention mention = CommentMention.builder()
                        .member(targetMember)
                        .comment(comment)
                        .build();
                commentMentionRepository.save(mention);

                mentionNotificationService.notifyToTargetMember(
                        memberService.getById(SecurityUtil.getCurrentMemberId()),
                        targetMember,
                        request.getPostId()
                );
            });
        }


    }

    @Transactional
    public void updateComment(CommentUpdateRequest request) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(request.getId())
                .orElseThrow(() -> new InvalidCommentIdException(INVALID_COMMENT_ID_ERROR));

        Member currentUser = memberService.getById(SecurityUtil.getCurrentMemberId());

        if (!comment.getAuthor().equals(currentUser) && !currentUser.getRole().equals("ADMIN")) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_ERROR);
        }

        comment.setContent(request.getContent());
        commentRepository.save(comment);

        List<CommentMention> existingMentions = commentMentionRepository.findAllByCommentId(comment.getId());
        List<String> newMentionNicknames = request.getMentions() != null ? request.getMentions() : List.of();

        existingMentions.stream()
                .filter(mention -> !newMentionNicknames.contains(mention.getMember().getNickname()))
                .forEach(commentMentionRepository::delete);

        newMentionNicknames.stream()
                .filter(nickname -> existingMentions.stream()
                        .noneMatch(mention -> mention.getMember().getNickname().equals(nickname)))
                .forEach(nickname -> {
                    Member targetMember = memberService.findByNickname(nickname);
                    if (targetMember == null) {
                        throw new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR);
                    }

                    CommentMention mention = CommentMention.builder()
                            .member(targetMember)
                            .comment(comment)
                            .build();
                    commentMentionRepository.save(mention);

                    mentionNotificationService.notifyToTargetMember(currentUser, targetMember, comment.getId());
                });
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new InvalidCommentIdException(INVALID_COMMENT_ID_ERROR));
        Member currentUser = memberService.getById(SecurityUtil.getCurrentMemberId());

        if (!comment.getAuthor().equals(currentUser) && !currentUser.getRole().equals("ADMIN")) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_ERROR);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    public List<CommentCardResponse> getDeletedComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(CommentCardResponse::from).toList();
    }

    @Transactional
    public void restoreDeletedComments(Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedTrue(commentId)
                .orElseThrow(() -> new InvalidCommentIdException(INVALID_COMMENT_ID_ERROR));
        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    @Transactional
    public void restoreCommentsByPostId(Long postId) {
        List<Comment> deletedComments = commentRepository.findAllByPostIdAndDeletedTrue(postId);

        if (deletedComments.isEmpty()) {
            throw new InvalidCommentIdException(INVALID_COMMENT_ID_ERROR);
        }

        deletedComments.forEach(comment -> comment.setDeleted(false));
        commentRepository.saveAll(deletedComments);
    }

    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdAndDeletedFalse(postId);
        comments.forEach(comment -> comment.setDeleted(true));
        commentRepository.saveAll(comments);
    }


}
