package org.durcit.be.system.exception.upload;

public class FileSizeExccedsMaximumLimitException extends RuntimeException {

    public FileSizeExccedsMaximumLimitException() {
    }

    public FileSizeExccedsMaximumLimitException(String message) {
        super(message);
    }

    public FileSizeExccedsMaximumLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSizeExccedsMaximumLimitException(Throwable cause) {
        super(cause);
    }
}
