package org.durcit.be.security.domian;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationInfo {
    private String code;
    private long expirationTime;
}
