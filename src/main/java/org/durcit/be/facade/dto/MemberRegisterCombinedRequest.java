package org.durcit.be.facade.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.security.dto.RegisterRequest;
import org.durcit.be.upload.dto.ProfileImageRequest;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterCombinedRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String nickname;

    private MultipartFile profileImage; // 프로필 이미지 파일
}
