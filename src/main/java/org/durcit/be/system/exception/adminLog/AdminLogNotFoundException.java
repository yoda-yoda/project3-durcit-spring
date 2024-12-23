package org.durcit.be.system.exception.adminLog;

public class AdminLogNotFoundException extends RuntimeException {
    public AdminLogNotFoundException(String message) {
        super(message);
    }

    public AdminLogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminLogNotFoundException(Throwable cause) {
        super(cause);
    }

    public AdminLogNotFoundException() {
    }
}
