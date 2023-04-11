package ch.uzh.ifi.hase.soprafs23.exceptions;

public class ApiConnectionError extends RuntimeException{
    public ApiConnectionError(String message, Throwable cause) {
        super(message, cause);
    }
    
}
