package org.durcit.be.comment.dto;

import jakarta.*;
import jakarta.persistence.Column;
import lombok.*;
import org.durcit.be.comment.domain.Comment;
import org.durcit.be.security.domian.Member;

import java.time.LocalDateTime;

// 컨트롤러 <-> 서비스 간 이동하는 객체는 반드시 DTO로 정의하기
// @PathVariable과 같은경우, 객체로 받지 않는다면, Long타입과 String도 가능
// 다만, 대부분 DTO 정의해서 가져오도록
// 컨트롤러 -> 서비스로 받는 DTO는 Request로 끝나도록
// 서비스 -> 컨트롤러로 내려주는 DTO는 Response로 끝나도록

// DTO는 뷰와 통신하여 자주 변경되므로 domain과 분리

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentRequest {

    private Long id;

    // 추가 후 테스트
//    private Long postId;
//
//    private Long memberId;

    private String comment;

    private String content;

    private boolean division;

    private LocalDateTime createdId = LocalDateTime.now();

    private LocalDateTime updateId;

}

