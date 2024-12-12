package org.durcit.be.security.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email
    private String email;
    private String password;
    private String nickname;
}
