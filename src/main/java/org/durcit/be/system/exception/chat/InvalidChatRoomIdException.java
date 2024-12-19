package org.durcit.be.system.exception.chat;

public class InvalidChatRoomIdException extends RuntimeException {
    public InvalidChatRoomIdException() {
      super();
    }

    public InvalidChatRoomIdException(Throwable cause) {
      super(cause);
    }

    public InvalidChatRoomIdException(String message, Throwable cause) {
      super(message, cause);
    }

    public InvalidChatRoomIdException(String message) {
      super(message);
    }
}
