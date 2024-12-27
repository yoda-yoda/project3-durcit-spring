package org.durcit.be.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentionResponse {

    private Long id;
    private String nickname;
    private String profileImage;
}
