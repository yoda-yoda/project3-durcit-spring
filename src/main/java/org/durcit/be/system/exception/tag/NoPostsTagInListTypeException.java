package org.durcit.be.system.exception.tag;

public class NoPostsTagInListTypeException extends RuntimeException {
    public NoPostsTagInListTypeException(String message) {
        super(message);
    }

  public NoPostsTagInListTypeException() {
    super();
  }

  public NoPostsTagInListTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoPostsTagInListTypeException(Throwable cause) {
    super(cause);
  }
}
