package ch.uzh.ifi.hase.soprafs23.Game.stateStorage;


import org.springframework.stereotype.Component;

import java.util.TimerTask;

public class Timer {
    private final int timeInSeconds;
    private int elapsedTimeInSeconds;

    private boolean isRunning;


    public Timer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        this.elapsedTimeInSeconds = 0;
    }

    public void start() {
        isRunning = true;
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                elapsedTimeInSeconds++;
                System.out.println("Remaining time: " + (timeInSeconds-elapsedTimeInSeconds) + " seconds");
            }
        };

        timer.scheduleAtFixedRate(task, 1000, 10000);
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