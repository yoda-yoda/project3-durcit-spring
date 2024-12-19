package org.durcit.be.system.exception.tagFollow;

public class TagFollowNotFoundException extends RuntimeException {
    public TagFollowNotFoundException(String message) {
        super(message);
    }

    public TagFollowNotFoundException() {
        super();
    }

    public TagFollowNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagFollowNotFoundException(Throwable cause) {
        super(cause);
    }
}
