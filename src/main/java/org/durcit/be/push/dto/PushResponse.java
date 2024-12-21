package org.durcit.be.push.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushResponse {

    private Long id;
    private String message;
    private Long postId;
    private boolean confirmed;
    private LocalDateTime createdAt;

}
