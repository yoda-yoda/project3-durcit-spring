package org.durcit.be.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRegisterRequest {

    private String nickname;
    private String title;
    private String content;

}
