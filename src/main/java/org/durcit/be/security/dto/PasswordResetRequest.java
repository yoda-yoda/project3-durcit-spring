package org.durcit.be.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
