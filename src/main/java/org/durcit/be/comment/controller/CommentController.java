package org.durcit.be.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.comment.dto.CommentCardResponse;
import org.durcit.be.comment.dto.CommentRegisterRequest;
import org.durcit.be.comment.dto.CommentUpdateRequest;
import org.durcit.be.comment.service.CommentService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/members/comments")
    public ResponseEntity<ResponseData<CommentCardResponse>> registerComment(@RequestBody CommentRegisterRequest request) {
        CommentCardResponse commentCardResponse = commentService.registerComment(request);
        return ResponseData.toResponseEntity(ResponseCode.CREATE_COMMENT_SUCCESS, commentCardResponse);
    }

    @PutMapping("/members/comments")
    public ResponseEntity<ResponseData> updateComment(@RequestBody CommentUpdateRequest request) {
        commentService.updateComment(request);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_COMMENT_SUCCESS);
    }

    @DeleteMapping("/members/comments/{commentId}")
    public ResponseEntity<ResponseData> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_COMMENT_SUCCESS);
    }

    @GetMapping("/members/my-comments/{memberId}")
    public ResponseEntity<ResponseData<List<CommentCardResponse>>> getCommentsByMemberId(@PathVariable Long memberId) {
        List<CommentCardResponse> responses = commentService.getCommentsByMemberId(memberId);
        return ResponseData.toResponseEntity(ResponseCode.GET_COMMENT_SUCCESS, responses);
    }

    @GetMapping("/admins/comments")
    public ResponseEntity<ResponseData<List<CommentCardResponse>>> getAllComments() {
        List<CommentCardResponse> allComments = commentService.getDeletedComments();
        return ResponseData.toResponseEntity(ResponseCode.GET_COMMENT_SUCCESS, allComments);
    }

    @PutMapping("/admins/comments/{commentId}")
    public ResponseEntity<ResponseData> restoreComment(@PathVariable Long commentId) {
        commentService.restoreDeletedComments(commentId);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_COMMENT_SUCCESS);
    }


}
