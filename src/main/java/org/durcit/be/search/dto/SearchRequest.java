package org.durcit.be.search.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {


    @NotNull
    private String search;


}
