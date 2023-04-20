package ch.uzh.ifi.hase.soprafs23.Game.stateStorage;


import org.springframework.stereotype.Component;

public class Timer {
    private final int timeInSeconds;
    private int elapsedTimeInSeconds;

    private boolean isRunning;


    public Timer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        this.elapsedTimeInSeconds = 0;
    }

    public void start() {
        try {
            for (int i = timeInSeconds; i > 0; i--) {
                isRunning = true;
                Thread.sleep(1000);
                elapsedTimeInSeconds++;
                System.out.println("Remaining time: " + (timeInSeconds-elapsedTimeInSeconds) + " seconds");
            }
        } catch (InterruptedException e) {
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