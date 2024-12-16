package org.durcit.be.system.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.SecurityUtil;
import org.durcit.be.system.aop.annotations.RequireCurrentMemberId;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.springframework.stereotype.Component;

import static org.durcit.be.system.exception.ExceptionMessage.MEMBER_NOT_FOUND_ERROR;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrentMemberAspect {

    private final MemberService memberService;

    @Before("@annotation(requireCurrentMemberId)")
    public void validateCurrentMember(RequireCurrentMemberId requireCurrentMemberId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (!memberService.isPresentById(memberId)) {
            throw new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR);
        }
    }

}
