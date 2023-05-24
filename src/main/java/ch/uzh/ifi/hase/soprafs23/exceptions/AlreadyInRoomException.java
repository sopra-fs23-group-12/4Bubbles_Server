package ch.uzh.ifi.hase.soprafs23.exceptions;

public class AlreadyInRoomException extends RuntimeException{
    public AlreadyInRoomException(String message) {
        super(message);
    }
}
