package net.phoenix1355.murder.room;

public class RoomException extends Exception {
    public RoomException(String message) {
        super(message);
    }

    public RoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
