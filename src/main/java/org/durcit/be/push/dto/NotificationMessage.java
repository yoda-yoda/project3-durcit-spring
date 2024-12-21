package org.durcit.be.push.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage {

    private Long messageReceiver;
    private String message;
    private Long postId;

}
