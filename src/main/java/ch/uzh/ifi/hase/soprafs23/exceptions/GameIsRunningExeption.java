package ch.uzh.ifi.hase.soprafs23.exceptions;

public class GameIsRunningExeption extends RuntimeException{
    public GameIsRunningExeption(String message) {
        super(message);
    }
    
}
