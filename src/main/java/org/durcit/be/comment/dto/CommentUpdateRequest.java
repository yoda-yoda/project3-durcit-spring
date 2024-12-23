package org.durcit.be.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {
    private Long id;
    private String content;
    private Long authorId;
    private List<String> mentions;
}
