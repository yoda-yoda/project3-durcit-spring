package org.durcit.be.system.exception.tag;

public class EmptyPostsTagListInPostException extends RuntimeException {
    public EmptyPostsTagListInPostException(String message) {
        super(message);
    }

    public EmptyPostsTagListInPostException() {
        super();
    }

    public EmptyPostsTagListInPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyPostsTagListInPostException(Throwable cause) {
        super(cause);
    }
}
