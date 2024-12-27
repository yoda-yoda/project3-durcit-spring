package org.durcit.be.system.exception.postSearch;

public class PostSearchNotFoundException extends RuntimeException {

    public PostSearchNotFoundException(String message) {
        super(message);
    }

    public PostSearchNotFoundException() {
        super();
    }

    public PostSearchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostSearchNotFoundException(Throwable cause) {
        super(cause);
    }
}
