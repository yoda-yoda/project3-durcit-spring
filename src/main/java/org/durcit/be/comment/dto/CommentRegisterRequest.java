package org.durcit.be.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRegisterRequest {
    private String content;
    private List<String> mentionList;
    private Long parentId;
}
