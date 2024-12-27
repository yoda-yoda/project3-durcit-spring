package org.durcit.be.system.exception.auth;

public class MemberBlockedException extends RuntimeException {

    public MemberBlockedException() {
        super();
    }

    public MemberBlockedException(String message) {
        super(message);
    }

    public MemberBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberBlockedException(Throwable cause) {
        super(cause);
    }
}
