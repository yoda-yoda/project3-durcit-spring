package org.durcit.be.security.config;

import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dto.TokenBody;
import org.durcit.be.security.service.JwtTokenProvider;
import org.durcit.be.system.exception.ExceptionMessage;
import org.durcit.be.system.exception.auth.InvalidUserException;
import org.durcit.be.system.exception.auth.NotValidTokenException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.stomp.StompCommand;

import static org.durcit.be.system.exception.ExceptionMessage.*;

@Component
@RequiredArgsConstructor
public class WebSocketAuthenticationInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                if (jwtTokenProvider.validate(token)) {
                    TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            tokenBody.getMemberId(),
                            null,
                            null
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    accessor.setUser(authentication);
                } else {
                    throw new NotValidTokenException(NOT_VALID_TOKEN_ERROR);
                }
            } else {
                throw new InvalidUserException(INVALID_USER_ERROR);
            }
        }
        return message;
    }
}
