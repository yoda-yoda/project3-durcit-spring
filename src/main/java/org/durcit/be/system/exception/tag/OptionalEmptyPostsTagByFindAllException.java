package org.durcit.be.system.exception.tag;

public class OptionalEmptyPostsTagByFindAllException extends RuntimeException {
    public OptionalEmptyPostsTagByFindAllException(String message) {
        super(message);
    }

    public OptionalEmptyPostsTagByFindAllException() {
        super();
    }

    public OptionalEmptyPostsTagByFindAllException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionalEmptyPostsTagByFindAllException(Throwable cause) {
        super(cause);
    }
}
