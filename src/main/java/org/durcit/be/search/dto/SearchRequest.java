package org.durcit.be.search.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {


    @NotNull
    // Post 안의 제목
    private String title;

    @NotNull
    // Post 안의 내용
    private String content;



    @NotNull
    // Tag의
    private String contents;



}
