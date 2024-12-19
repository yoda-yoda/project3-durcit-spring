package org.durcit.be.security.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.security.dto.MemberDetails;
import org.durcit.be.system.exception.auth.NoAuthenticationInSecurityContextException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static org.durcit.be.system.exception.ExceptionMessage.NO_AUTHENTICATION_IN_SECURITY_CONTEXT_ERROR;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getPrincipal() == "anonymousUser") {
            return null;
        }
        return ((MemberDetails) authentication.getPrincipal()).getId();
    }

    public static String getCurrentMemberIdOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }

    public static String getCurrentMemberName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new NoAuthenticationInSecurityContextException(NO_AUTHENTICATION_IN_SECURITY_CONTEXT_ERROR);
        }
        return authentication.getName();
    }
}
