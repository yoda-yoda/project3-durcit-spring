package org.durcit.be.system.exception.tagFollow;

public class NoTagFollowInListTypeException extends RuntimeException {
    public NoTagFollowInListTypeException(String message) {
        super(message);
    }

    public NoTagFollowInListTypeException() {
        super();
    }

    public NoTagFollowInListTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTagFollowInListTypeException(Throwable cause) {
        super(cause);
    }
}
