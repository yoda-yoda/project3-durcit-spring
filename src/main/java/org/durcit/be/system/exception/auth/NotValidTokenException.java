package org.durcit.be.system.exception.auth;

public class NotValidTokenException extends RuntimeException {
    public NotValidTokenException() {
        super();
    }

    public NotValidTokenException(String message) {
        super(message);
    }

    public NotValidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidTokenException(Throwable cause) {
        super(cause);
    }
}
