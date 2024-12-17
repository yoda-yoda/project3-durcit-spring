package org.durcit.be.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.security.dto.RegisterRequest;
import org.durcit.be.upload.dto.ProfileImageRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterCombinedRequest {

    private RegisterRequest registerRequest;
    private ProfileImageRequest profileImageRequest;

}
