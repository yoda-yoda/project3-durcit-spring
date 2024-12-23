package org.durcit.be.system.exception.chat;

public class InvalidChatRoomCreationException extends RuntimeException {

    public InvalidChatRoomCreationException() {
        super();
    }

    public InvalidChatRoomCreationException(String message) {
        super(message);
    }

    public InvalidChatRoomCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidChatRoomCreationException(Throwable cause) {
        super(cause);
    }
}
