package org.durcit.be.system.exception.tag;

public class OptionalEmptyPostsTagFindByIdException extends RuntimeException  {

    public OptionalEmptyPostsTagFindByIdException() {
        super();
    }

    public OptionalEmptyPostsTagFindByIdException(String message) {
        super(message);
    }

    public OptionalEmptyPostsTagFindByIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionalEmptyPostsTagFindByIdException(Throwable cause) {
        super(cause);
    }


}
