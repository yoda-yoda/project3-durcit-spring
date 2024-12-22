package org.durcit.be.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TagSearchRequest {

    @Setter
    @NotNull
    private String contents;

}
