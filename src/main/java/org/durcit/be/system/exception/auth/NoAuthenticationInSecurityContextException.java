package org.durcit.be.system.exception.auth;

public class NoAuthenticationInSecurityContextException extends RuntimeException {
    public NoAuthenticationInSecurityContextException() {
        super();
    }

    public NoAuthenticationInSecurityContextException(String message) {
        super(message);
    }

    public NoAuthenticationInSecurityContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAuthenticationInSecurityContextException(Throwable cause) {
        super(cause);
    }
}
