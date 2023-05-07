package ch.uzh.ifi.hase.soprafs23.game.stateStorage;


import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.service.SocketBasics;

public class Timer {
    private final int timeInSeconds;
    private int elapsedTimeInSeconds;
    SocketBasics socketBasics = new SocketBasics();

    private boolean isRunning;


    public Timer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        this.elapsedTimeInSeconds = 0;
    }

    public void start(String roomCode) {
        
        try {
            for (int i = timeInSeconds; i > 0; i--) {
                isRunning = true;
                Thread.sleep(1000);
                elapsedTimeInSeconds++;
                socketBasics.sendObjectToRoom(roomCode, EventNames.TIMER_COUNT.eventName, i);
                System.out.println("Remaining time: " + (timeInSeconds-elapsedTimeInSeconds) + " seconds");
                 
                }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //Error handling needs to be implemented here and improved in general
        }
        isRunning = false;
        
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public int getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getRemainingTimeInSeconds() {
        return timeInSeconds-elapsedTimeInSeconds;
    }
}