package org.durcit.be.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostsContentSearchRequest {

    @Setter
    @NotNull
    private String content;
}
