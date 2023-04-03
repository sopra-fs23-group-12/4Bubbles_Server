package ch.uzh.ifi.hase.soprafs23.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
