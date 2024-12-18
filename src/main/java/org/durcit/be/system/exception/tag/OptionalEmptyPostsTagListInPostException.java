package org.durcit.be.system.exception.tag;

public class OptionalEmptyPostsTagListInPostException extends RuntimeException {
    public OptionalEmptyPostsTagListInPostException(String message) {
        super(message);
    }

    public OptionalEmptyPostsTagListInPostException() {
        super();
    }

    public OptionalEmptyPostsTagListInPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionalEmptyPostsTagListInPostException(Throwable cause) {
        super(cause);
    }
}
