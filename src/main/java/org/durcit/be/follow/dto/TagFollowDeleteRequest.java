package org.durcit.be.follow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagFollowDeleteRequest {

    @NotNull
    private String tag;

}
