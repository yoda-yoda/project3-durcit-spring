package org.durcit.be.system.exception.push;

public class PushNotFoundException extends RuntimeException {
    public PushNotFoundException() {
        super();
    }

    public PushNotFoundException(String message) {
        super(message);
    }

    public PushNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PushNotFoundException(Throwable cause) {
        super(cause);
    }
}
