package org.durcit.be.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtConfiguration {

    private final Validation validation;
    private final Secret secret;

    @RequiredArgsConstructor
    @Getter
    public static class Validation {
        private final Long access;
        private final Long refresh;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Secret {
        private final String appKey;
        private final String originKey;
    }

}
