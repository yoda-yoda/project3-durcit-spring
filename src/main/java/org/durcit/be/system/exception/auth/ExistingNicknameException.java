package org.durcit.be.system.exception.auth;

public class ExistingNicknameException extends RuntimeException {

    public ExistingNicknameException() {
    }

    public ExistingNicknameException(String message) {
        super(message);
    }

    public ExistingNicknameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingNicknameException(Throwable cause) {
        super(cause);
    }
}
