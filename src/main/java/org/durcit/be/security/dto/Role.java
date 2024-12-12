package org.durcit.be.security.dto;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"), MANAGER("MANAGER"), MEMBER("MEMBER");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
