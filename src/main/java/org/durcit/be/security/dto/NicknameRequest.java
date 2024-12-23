package org.durcit.be.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NicknameRequest {


    @NotNull
    private String nickname;



}
