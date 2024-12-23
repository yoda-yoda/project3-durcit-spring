package org.durcit.be.system.exception.comment;

public class InvalidCommentIdException extends RuntimeException {
    public InvalidCommentIdException() {
    }

    public InvalidCommentIdException(String message) {
        super(message);
    }

    public InvalidCommentIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCommentIdException(Throwable cause) {
        super(cause);
    }
}
