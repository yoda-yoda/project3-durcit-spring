package org.durcit.be.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.domian.RefreshToken;
import org.durcit.be.security.dto.KeyPair;
import org.durcit.be.security.dto.MemberDetails;
import org.durcit.be.security.service.JwtTokenProvider;
import org.durcit.be.security.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandlerFilter extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${custom.jwt.redirection.base-url}")
    private String baseUrl;

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 성공했을 때
        // oauth2.0 인증성공시
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();

        HashMap<String, String> params = new HashMap<>();

        RefreshToken findRefreshToken = jwtTokenProvider.validateRefreshToken(principal.getId());

        if ( findRefreshToken == null ) {
            Member findMember = memberService.getById(principal.getId());
            KeyPair keyPair = jwtTokenProvider.generateKeyPair(findMember);
            params.put("access", keyPair.getAccessToken());
            params.put("refresh", keyPair.getRefreshToken());
        } else {
            String accessToken = jwtTokenProvider.issueAccessToken(principal.getId(), principal.getRole());
            params.put("access", accessToken);
            params.put("refresh", findRefreshToken.getRefreshToken());
        }

        String targetUrl = genUrlStr(params);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

    private String genUrlStr(HashMap<String, String> params) {
        return UriComponentsBuilder.fromUri(URI.create(baseUrl))
                .queryParam("access", params.get("access"))
                .queryParam("refresh", params.get("refresh"))
                .build().toUriString();
    }
}
