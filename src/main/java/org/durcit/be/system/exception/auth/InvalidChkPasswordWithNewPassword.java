package org.durcit.be.system.exception.auth;

public class InvalidChkPasswordWithNewPassword extends RuntimeException{

    public InvalidChkPasswordWithNewPassword() {
        super();
    }

    public InvalidChkPasswordWithNewPassword(String message) {
        super(message);
    }

    public InvalidChkPasswordWithNewPassword(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidChkPasswordWithNewPassword(Throwable cause) {
        super(cause);
    }
}
