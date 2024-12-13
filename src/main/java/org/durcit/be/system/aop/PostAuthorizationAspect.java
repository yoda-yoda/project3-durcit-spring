package org.durcit.be.system.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.service.PostService;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.system.aop.annotations.PostRequireAuthorization;
import org.durcit.be.system.exception.auth.InvalidUserException;
import org.durcit.be.system.exception.auth.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.durcit.be.system.exception.ExceptionMessage.INVALID_USER_ERROR;
import static org.durcit.be.system.exception.ExceptionMessage.UNAUTHORIZED_ACCESS_ERROR;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PostAuthorizationAspect {

    private final MemberService memberService;
    private final PostService postService;

    @Before(value = "@annotation(requireAuthorization)", argNames = "requireAuthorization,joinPoint")
    public void authorize(PostRequireAuthorization requireAuthorization, JoinPoint joinPoint) {

        log.info("PostAuthorizationAspect");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidUserException(INVALID_USER_ERROR);
        }

        Long currentMemberId = Long.valueOf(authentication.getName());
        if (requireAuthorization.roles().length > 0) {
            boolean hasRole = memberService.hasRole(currentMemberId, requireAuthorization.roles());
            if (hasRole) {
                return;
            }
        }

        String parameterName = requireAuthorization.parameter();
        Long postId = extractParameter(joinPoint, parameterName);
        Post post = postService.getById(postId);
        if (!post.getMember().getId().equals(currentMemberId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_ERROR);
        }

    }

    private Long extractParameter(JoinPoint joinPoint, String parameterName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterName.equals(parameterNames[i]) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }
        return null;
    }

}
