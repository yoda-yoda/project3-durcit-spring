package org.durcit.be.system.exception.tag;

public class OptionalEmptyPostsTagByContentsException extends RuntimeException {
    public OptionalEmptyPostsTagByContentsException(String message) {
        super(message);
    }

    public OptionalEmptyPostsTagByContentsException() {
        super();
    }

    public OptionalEmptyPostsTagByContentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionalEmptyPostsTagByContentsException(Throwable cause) {
        super(cause);
    }
}
