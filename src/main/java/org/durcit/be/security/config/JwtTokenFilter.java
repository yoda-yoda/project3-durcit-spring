package org.durcit.be.security.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.durcit.be.security.domian.MemberDetails;
import org.durcit.be.security.dto.TokenBody;
import org.durcit.be.security.service.JwtTokenProvider;
import org.durcit.be.security.service.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String realToken = resolveToken(request);

        if (realToken != null && jwtTokenProvider.validate(realToken)) {
            TokenBody tokenBody = jwtTokenProvider.parseJwt(realToken);
            MemberDetails memberDetails = memberService.loadMemberDetails(tokenBody.getMemberId());
            log.info("memberDetails.getId() = {}", memberDetails.getId());
            Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, realToken, memberDetails.getAuthorities());
            log.info("authentication.getPrincipal() = {}", ((MemberDetails) authentication.getPrincipal()).getEmail());
            log.info("authentication.getPrincipal() = {}", ((MemberDetails) authentication.getPrincipal()).getId());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
